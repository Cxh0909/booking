package org.jboss.quickstarts.wfk.customer;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.quickstarts.wfk.area.InvalidAreaCodeException;
import org.jboss.quickstarts.wfk.contact.Contact;
import org.jboss.quickstarts.wfk.contact.ContactService;
import org.jboss.quickstarts.wfk.contact.UniqueEmailException;
import org.jboss.quickstarts.wfk.util.RestServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/customers", description = "Operations about customers")
@Stateless
public class CustomerRestService {
	
	@Inject
    private @Named("logger") Logger log;
    
    @Inject
    private CustomerService service;
	
	@SuppressWarnings("unused")
    @POST
    @ApiOperation(value = "Add a new Contact to the database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Customer created successfully."),
            @ApiResponse(code = 400, message = "Invalid Customer supplied in request body"),
            @ApiResponse(code = 409, message = "Customer supplied in request body conflicts with an existing Customer"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response createCustomer(
            @ApiParam(value = "JSON representation of Customer object to be added to the database", required = true)
            Customer customer) {


        if (customer == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {
            // Go add the new Customer.
            service.create(customer);

            // Create a "Resource Created" 201 Response and pass the customer back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(customer);


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

        log.info("createCustomer completed. Contact = " + customer.toString());
        return builder.build();
    }
}
