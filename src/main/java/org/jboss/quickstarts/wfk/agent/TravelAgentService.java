package org.jboss.quickstarts.wfk.agent;

import javax.inject.Inject;
import org.jboss.quickstarts.wfk.area.AreaService;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.guestbooking.GuestBooking;
import org.jboss.quickstarts.wfk.guestbooking.GuestBookingService;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

/**
 * @author yu zhang
 */
public class TravelAgentService {

    private static final String FLIGHT_URL = "https://csc-8104-sajith-sajeev-retnamma-sajithsajeevruni-dev.apps"
            + ".sandbox.x8i5.p1.openshiftapps.com/";
    private static final Long CUSTOMER_ID_FLIGHT = 1L;

    private static final Long CUSTOMER_ID_HOTEL = 2L;

    private ResteasyClient client;

    public TravelAgentService() {
        client = new ResteasyClientBuilder().build();
    }

    //    @Inject
//    private HotelRestClient hotelRestClient;
//
//    @Inject
//    private FlightRestClient flightRestClient;

    @Inject
    private GuestBookingService bookingService;
    public Booking create(TravelInfo travelInfo) throws Exception {
        Customer customer = new Customer();
        if (travelInfo.getCommodityType() == CommodityType.TAXI) {
            customer.setId(travelInfo.getCustomerId());
            GuestBooking guestBooking = new GuestBooking();
            guestBooking.setCustomer(customer);
            return bookingService.create(guestBooking);
        }
        if (travelInfo.getCommodityType() == CommodityType.FLIGHT) {
            customer.setId(CUSTOMER_ID_FLIGHT);
            GuestBooking guestBooking = new GuestBooking();
            guestBooking.setCustomer(customer);
            ResteasyWebTarget target = client.target("http://ec2-18-119-125-232.us-east-2.compute.amazonaws.com/");
            AreaService service = target.proxy(FlightRe.class);
            //return flightRestClient.create(guestBooking);
        } else {
            customer.setId(CUSTOMER_ID_HOTEL);
            GuestBooking guestBooking = new GuestBooking();
            guestBooking.setCustomer(customer);
            //return hotelRestClient.create(guestBooking);
        }
        return null;
    }
}
