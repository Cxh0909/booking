package org.jboss.quickstarts.wfk.booking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.quickstarts.wfk.area.InvalidAreaCodeException;
import org.jboss.quickstarts.wfk.contact.UniqueEmailException;
import org.jboss.quickstarts.wfk.util.RestServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/bookings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/bookings", description = "Operations about bookings")
@Stateless
public class BookingRestService {
	
	@Inject
    private @Named("logger") Logger log;
    
    @Inject
    private BookingService service;
	
	@SuppressWarnings("unused")
    @POST
    @ApiOperation(value = "Add a new Booking to the database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Booking created successfully."),
            @ApiResponse(code = 400, message = "Invalid Booking supplied in request body"),
            @ApiResponse(code = 409, message = "Booking supplied in request body conflicts with an existing Booking"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response createBooking(
            @ApiParam(value = "CustomerId of Booking object to be added to the database", required = true)
            @QueryParam("customerId") Long customerId,
			@ApiParam(value = "CommodityId of Booking object to be added to the database", required = true) @QueryParam("commodityId") Long commodityId) {


        if (customerId == null || commodityId == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {
            // Go add the new Booking.
            Booking booking = service.create(customerId, commodityId);

            // Create a "Resource Created" 201 Response and pass the Booking back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(booking);
			log.info("createBooking completed. Booking = " + booking.toString());
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
        } catch (InvalidAreaCodeException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("area_code", "The telephone area code provided is not recognised, please provide another");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, e);
        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }


        return builder.build();
    }

	@DELETE
	@Path("/cancelation/{id:[0-9]+}")
	@ApiOperation(value = "Cancel a Booking")
	public Response cancelABooking(@PathParam("id") Long id) {
		try {
			Booking booking = service.cancelById(id);
			return Response.ok(booking).build();
		}catch (Exception e) {
			throw new RestServiceException(e);
		}
	}

	@PUT
	@Path("/{id:[0-9]+}")
	@ApiOperation(value = "Update a Booking in the database")
	public Response updateBooking(
			@ApiParam(value = "Id of Booking to be updated", allowableValues = "range[0, infinity]", required = true) @PathParam("id") long id,
			@ApiParam(value = "JSON representation of Booking object to be updated in the database", required = true) Booking booking) {

		if (booking == null || booking.getId() == null) {
			throw new RestServiceException("Invalid Booking supplied in request body", Response.Status.BAD_REQUEST);
		}

		if (booking.getId() != null && booking.getId() != id) {
			// The client attempted to update the read-only Id. This is not permitted.
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("id", "The Booking ID in the request body must match that of the Booking being updated");
			throw new RestServiceException("Booking details supplied in request body conflict with another Booking",
					responseObj, Response.Status.CONFLICT);
		}

		if (service.findById(booking.getId()) == null) {
			// Verify that the booking exists. Return 404, if not present.
			throw new RestServiceException("No Booking with the id " + id + " was found!", Response.Status.NOT_FOUND);
		}

		Response.ResponseBuilder builder;

		try {
			// Apply the changes the Booking.
			service.update(booking);

			// Create an OK Response and pass the booking back in case it is needed.
			builder = Response.ok(booking);

		} catch (ConstraintViolationException ce) {
			// Handle bean validation issues
			Map<String, String> responseObj = new HashMap<>();

			for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
				responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
			}
			throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);
		} catch (UniqueEmailException e) {
			// Handle the unique constraint violation
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("email", "That email is already used, please use a unique email");
			throw new RestServiceException("Booking details supplied in request body conflict with another Booking",
					responseObj, Response.Status.CONFLICT, e);
		} catch (InvalidAreaCodeException e) {
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("area_code", "The telephone area code provided is not recognised, please provide another");
			throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, e);
		} catch (Exception e) {
			// Handle generic exceptions
			throw new RestServiceException(e);
		}

		log.info("updateBooking completed. Booking = " + booking.toString());
		return builder.build();
	}

	@GET
	@ApiOperation(value = "Fetch all Bookings", notes = "Returns a JSON array of all stored Booking objects.")
	public Response retrieveAllBookings() {
		try {
			List<Booking> bookings = service.findAllOrderedById();
			return Response.ok(bookings).build();
		}catch (Exception e) {
			throw new RestServiceException(e);
		}
	}
}
