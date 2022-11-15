package org.jboss.quickstarts.wfk.guestbooking;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.booking.BookingService;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.customer.CustomerService;
import org.jboss.quickstarts.wfk.taxi.Taxi;
import org.jboss.quickstarts.wfk.taxi.TaxiService;
import org.jboss.quickstarts.wfk.taxi.TaxiStatus;
import org.jboss.quickstarts.wfk.util.RestServiceException;

/**
 * @author yu zhang
 */
@Path("/guestBookings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/guestBookings", description = "Operations about guestBookings")
@Stateless
public class GuestBookingRestService {

    @Inject
    private GuestBookingService guestBookingService;

    @POST
    @Transactional
    public Response guestBooking(@ApiParam(value = "JSON representation of GuestBooking object to be added to the "
            + "database",
            required = true) GuestBooking guestBooking) throws Exception {
        try {
            Booking booking = guestBookingService.create(guestBooking);
            return Response.ok(booking).build();
        } catch (Exception e) {
            throw new RestServiceException(e);
        }
    }
}
