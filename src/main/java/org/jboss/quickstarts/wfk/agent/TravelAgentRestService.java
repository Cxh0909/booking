package org.jboss.quickstarts.wfk.agent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.quickstarts.wfk.area.InvalidAreaCodeException;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.contact.UniqueEmailException;
import org.jboss.quickstarts.wfk.exception.DateFormatException;
import org.jboss.quickstarts.wfk.exception.DateRangeException;
import org.jboss.quickstarts.wfk.util.RestServiceException;


@Path("/agent")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/agent", description = "Operations about bookings")
@Stateless
public class TravelAgentRestService {
    // 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
    // oc login --token=sha256~wXOX7ouC7Ds9sNwD47XUzR1xSDYRzoZn-H-i5L08gMA --server=https://api.sandbox.x8i5.p1.openshiftapps.com:6443
    // oc new-build eap73-openjdk11~"https://github.com/Cxh0909/booking.git" --name=csc8104-build --to=csc8104-build-stream --source-secret=github-secret
    @Inject
    private TravelAgentService travelAgentService;

    @POST
    @ApiOperation(value = "create a booking using agent")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Booking created successfully."),
            @ApiResponse(code = 400, message = "Invalid Booking supplied in request body"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response createBooking(@ApiParam()TravelInfo travelInfo) {

        try {
            travelAgentService.create(travelInfo);
            return Response.status(Response.Status.CREATED).build();
        } catch (ConstraintViolationException ce) {
            Map<String, String> responseObj = new HashMap<>();
            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);
        } catch (IllegalArgumentException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("booking", "failed to booking, please confirm your booking params");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, e);
        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }
    }
}
