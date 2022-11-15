package org.jboss.quickstarts.wfk.agent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.util.RestServiceException;

/**
 * @author yu zhang
 */
@Path("/agent")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/agent", description = "Operations about bookings")
@Stateless
public class TravelAgentRestService {

    @Inject
    private TravelAgentService travelAgentService;

    @POST
    public Response createBooking(@ApiParam()TravelInfo travelInfo) {
        try {
            Booking booking = travelAgentService.create(travelInfo);
            return Response.ok(booking).build();
        } catch (Exception e) {
            throw new RestServiceException(e);
        }
    }
}
