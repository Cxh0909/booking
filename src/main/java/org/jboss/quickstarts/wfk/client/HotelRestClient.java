package org.jboss.quickstarts.wfk.client;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.guestbooking.GuestBooking;

/**
 * @author yu zhang
 */
@Path("/hotels")
public interface HotelRestClient {

    @POST
    Booking create(GuestBooking booking);
}
