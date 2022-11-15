package org.jboss.quickstarts.wfk.agent;

import javax.inject.Inject;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.client.FlightRestClient;
import org.jboss.quickstarts.wfk.client.HotelRestClient;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.guestbooking.GuestBooking;
import org.jboss.quickstarts.wfk.guestbooking.GuestBookingService;

/**
 * @author yu zhang
 */
public class TravelAgentService {

    private static final Long CUSTOMER_ID_FLIGHT = 1L;

    private static final Long CUSTOMER_ID_HOTEL = 2L;

    @Inject
    private HotelRestClient hotelRestClient;

    @Inject
    private FlightRestClient flightRestClient;

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
            return flightRestClient.create(guestBooking);
        } else {
            customer.setId(CUSTOMER_ID_HOTEL);
            GuestBooking guestBooking = new GuestBooking();
            guestBooking.setCustomer(customer);
            return hotelRestClient.create(guestBooking);
        }
    }
}
