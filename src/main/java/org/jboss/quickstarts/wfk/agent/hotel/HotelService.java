package org.jboss.quickstarts.wfk.agent.hotel;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
public interface HotelService {
    @POST
    @Path("/bookings")
    void booking(HotelBookingRequest request);
}
