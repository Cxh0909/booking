package org.jboss.quickstarts.wfk.agent.flight;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/flights")
@Produces(MediaType.APPLICATION_JSON)
public interface FlightService {

    @POST
    @Path("/records")
    FlightBooking create(FlightBooking request);
}
