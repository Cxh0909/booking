package org.jboss.quickstarts.wfk.guestbooking;

import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.booking.BookingService;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.customer.CustomerService;
import org.jboss.quickstarts.wfk.taxi.Taxi;
import org.jboss.quickstarts.wfk.taxi.TaxiService;


@Dependent
public class GuestBookingService {
    @Inject
    private CustomerService customerService;
    @Inject
    private BookingService bookingService;

    @Inject
    private TaxiService taxiService;

    public Booking create(GuestBooking guestBooking) throws Exception {
        Customer customer = guestBooking.getCustomer();
        if (customer != null && customer.getId() != null) {
            customer = customerService.findById(customer.getId());
        }

        if (customer == null) {
            customer = customerService.create(guestBooking.getCustomer());
        }

        Booking booking = guestBooking.getBooking();
        booking.setCustomer(customer);
        Taxi taxi = taxiService.pickAFreeTaxi();
        booking.setTaxi(taxi);
        return bookingService.create(booking);
    }
}
