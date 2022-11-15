package org.jboss.quickstarts.wfk.guestbooking;

import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.customer.Customer;

/**
 * @author yu zhang
 */
public class GuestBooking {
    private Customer customer;

    private Booking booking;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
