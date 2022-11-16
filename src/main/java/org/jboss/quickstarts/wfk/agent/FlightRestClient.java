package org.jboss.quickstarts.wfk.agent;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.guestbooking.GuestBooking;

/**
 * @author yu zhang
 */
@Path("/flights")
@Produces(MediaType.APPLICATION_JSON)
public interface FlightRestClient {

    @POST
    Booking create(GuestBooking booking);
}
