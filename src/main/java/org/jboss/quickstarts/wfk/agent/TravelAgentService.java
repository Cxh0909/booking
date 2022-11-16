package org.jboss.quickstarts.wfk.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;
import org.jboss.quickstarts.wfk.agent.flight.FlightBookingRequest;
import org.jboss.quickstarts.wfk.agent.flight.FlightService;
import org.jboss.quickstarts.wfk.agent.hotel.HotelBookingRequest;
import org.jboss.quickstarts.wfk.agent.hotel.HotelService;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.booking.BookingService;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.guestbooking.GuestBooking;
import org.jboss.quickstarts.wfk.guestbooking.GuestBookingService;
import org.jboss.quickstarts.wfk.util.TimeUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;


public class TravelAgentService {

    private static final String HOTEL_URL = "https://csc-8104-abisek-mishra-abisekmishra-dev.apps.sandbox.x8i5.p1.openshiftapps.com/";

    private static final String FLIGHT_URL = "https://csc-8104-abisek-mishra-abisekmishra-dev.apps.sandbox.x8i5.p1"
            + ".openshiftapps.com/";
    private static final Long CUSTOMER_ID_FLIGHT = 1L;

    private static final Long CUSTOMER_ID_HOTEL = 1L;

    private ObjectMapper objectMapper;

    @Inject
    private BookingService bookingService;

    @Inject
    private TravelInfoValidator travelInfoValidator;

    private ResteasyClient client;

    public TravelAgentService() {
        client = new ResteasyClientBuilder().build();
        objectMapper = new ObjectMapper();
    }

    public void create(TravelInfo travelInfo) throws IllegalAccessException {

        travelInfoValidator.validateTravelInfo(travelInfo);

        if (travelInfo.getCommodityType() == CommodityType.TAXI) {
            try {
                Booking booking = objectMapper.convertValue(travelInfo.getBookingParams(), Booking.class);
                bookingService.create(booking);
            } catch (Exception e) {
                throw new IllegalAccessException("failed to booking");
            }
        }
        if (travelInfo.getCommodityType() == CommodityType.FLIGHT) {
            try {
                FlightBookingRequest req = objectMapper.convertValue(travelInfo.getBookingParams(),
                        FlightBookingRequest.class);
                ResteasyWebTarget target = client.target(FLIGHT_URL);
                FlightService flightService = target.proxy(FlightService.class);
                flightService.create(req);
            } catch (Exception e) {
                throw new IllegalAccessException("failed to booking");
            }

        } else {
            try {
                HotelBookingRequest req = objectMapper.convertValue(travelInfo.getBookingParams(),
                        HotelBookingRequest.class);
                ResteasyWebTarget target = client.target(HOTEL_URL);
                HotelService hotelService = target.proxy(HotelService.class);
                hotelService.booking(req);
            } catch (Exception e) {
                throw new IllegalAccessException("failed to booking");
            }
        }
    }
}
