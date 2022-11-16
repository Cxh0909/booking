package org.jboss.quickstarts.wfk.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.quickstarts.wfk.agent.flight.FlightBooking;
import org.jboss.quickstarts.wfk.agent.flight.FlightService;
import org.jboss.quickstarts.wfk.agent.hotel.HotelBooking;
import org.jboss.quickstarts.wfk.agent.hotel.HotelService;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.booking.BookingService;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;


public class TravelAgentService {

    private static final String HOTEL_URL = "https://csc-8104-abisek-mishra-abisekmishra-dev.apps.sandbox.x8i5.p1.openshiftapps.com/";

    private static final String FLIGHT_URL = "https://csc-8104-sajith-sajeev-retnamma-sajithsajeevruni-dev.apps.sandbox.x8i5.p1.openshiftapps.com/";
    private static final Long CUSTOMER_ID_FLIGHT = 1L;

    private static final Long CUSTOMER_ID_HOTEL = 1L;

    private ObjectMapper objectMapper;

    @Inject
    private @Named("logger") Logger log;

    @Inject
    private BookingService bookingService;

    @Inject
    private TravelInfoValidator travelInfoValidator;

    @Inject
    private TravelAgentRepository travelAgentRepository;

    private ResteasyClient client;

    public TravelAgentService() {
        client = new ResteasyClientBuilder().build();
        objectMapper = new ObjectMapper();
    }

    public TravelAgent create(TravelInfo travelInfo) throws IllegalAccessException {
        travelInfoValidator.validateTravelInfo(travelInfo);
        log.info("TravelAgentService.create() - Creating " + travelInfo);
        if (travelInfo.getCommodityType() == CommodityType.TAXI) {
            try {
                Booking booking = travelInfo.getTaxiBooking();
                bookingService.create(booking);
            } catch (Exception e) {
                throw new IllegalAccessException("failed to booking");
            }
        }
        if (travelInfo.getCommodityType() == CommodityType.FLIGHT) {
            try {
                FlightBooking req = travelInfo.getFlightBooking();
                long customerId = req.getCustomer().getId();
                req.getCustomer().setId(CUSTOMER_ID_FLIGHT);
                ResteasyWebTarget target = client.target(FLIGHT_URL);
                FlightService flightService = target.proxy(FlightService.class);
                FlightBooking resp = flightService.create(req);

                TravelAgent agent = new TravelAgent();
                agent.setCommodityType(CommodityType.FLIGHT);
                agent.setBookingDetail(objectMapper.writeValueAsString(resp));
                agent.setCustomerId(customerId);
                travelAgentRepository.create(agent);
                return agent;
            } catch (Exception e) {
                throw new IllegalAccessException("failed to booking");
            }

        } else {
            try {
                HotelBooking req = travelInfo.getHotelBooking();
                long customerId = req.getCustomerId();
                ResteasyWebTarget target = client.target(HOTEL_URL);
                HotelService hotelService = target.proxy(HotelService.class);
                HotelBooking resp = hotelService.booking(req);

                TravelAgent travelAgent = new TravelAgent();
                travelAgent.setCustomerId(customerId);
                travelAgent.setCommodityType(CommodityType.HOTEL);
                travelAgent.setBookingDetail(objectMapper.writeValueAsString(resp));
                return travelAgent;
            } catch (Exception e) {
                throw new IllegalAccessException("failed to booking");
            }
        }
    }

    public List<TravelAgent> findAllOrderedById() throws Exception {
        return travelAgentRepository.findAllOrderedById();
    }

    public TravelAgent findById(Long customerId) throws Exception {
        return travelAgentRepository.findByCustomerId(customerId);
    }
}
