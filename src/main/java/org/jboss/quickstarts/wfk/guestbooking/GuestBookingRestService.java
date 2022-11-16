package org.jboss.quickstarts.wfk.guestbooking;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.booking.BookingService;
import org.jboss.quickstarts.wfk.contact.UniqueEmailException;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.customer.CustomerService;
import org.jboss.quickstarts.wfk.exception.DateFormatException;
import org.jboss.quickstarts.wfk.exception.DateRangeException;
import org.jboss.quickstarts.wfk.exception.UniquePhoneNumberException;
import org.jboss.quickstarts.wfk.taxi.Taxi;
import org.jboss.quickstarts.wfk.taxi.TaxiService;
import org.jboss.quickstarts.wfk.taxi.TaxiStatus;
import org.jboss.quickstarts.wfk.util.RestServiceException;


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
    @ApiOperation(value = "guest booking, if you are a guest, we will create a account for you")
    @ApiResponses({
            @ApiResponse(code = 201, message = "GuestBooking created successfully."),
            @ApiResponse(code = 400, message = "Invalid Booking supplied in request body"),
            @ApiResponse(code = 409, message = "Booking start date is after end date"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response guestBooking(@ApiParam(value = "JSON representation of GuestBooking object to be added to the "
            + "database",
            required = true) GuestBooking guestBooking) {
        try {
            Booking booking = guestBookingService.create(guestBooking);
            return Response.status(Response.Status.CREATED).entity(booking).build();
        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);
        } catch (UniqueEmailException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "That email is already used, please use a unique email");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        } catch (UniquePhoneNumberException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("phoneNumber", "That phoneNumber is already used, please use a unique phoneNumber");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, e);
        } catch (DateFormatException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("date", "That date's format must like 'yyyy-MM-dd HH:mm:ss'");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        } catch (DateRangeException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("date", "The bookingStartDate must before bookingEndDate");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, e);
        } catch (RestServiceException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("taxi", "can't find a free taxi now");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, e);
        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }
    }
}
