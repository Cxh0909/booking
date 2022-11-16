package org.jboss.quickstarts.wfk.taxi;

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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

@Path("/taxis")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/taxis", description = "Operations about taxis")
@Stateless
public class TaxiRestService {
	
	@Inject
    private @Named("logger") Logger log;
    
    @Inject
    private TaxiService service;
	
	@SuppressWarnings("unused")
    @POST
    @ApiOperation(value = "Add a new Taxi to the database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Taxi created successfully."),
            @ApiResponse(code = 400, message = "Invalid Taxi supplied in request body"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response createTaxi(
            @ApiParam(value = "JSON representation of Taxi object to be added to the database", required = true)
            Taxi taxi) {

        if (taxi == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {
            // Go add the new Taxi.
            service.create(taxi);

            // Create a "Resource Created" 201 Response and pass the taxi back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(taxi);
        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();
            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);
        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        log.info("createTaxi completed. Taxi = " + taxi);
        return builder.build();
    }

    @GET
    @ApiOperation(value = "List all taxis in database, ordered by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request is processed normally"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response listAllTaxis() {
        try {
            List<Taxi> taxis = service.findAllOrderedById();
            return Response.ok(taxis).build();
        }catch (Exception e){
            throw new RestServiceException(e);
        }
    }

    @DELETE
    @Path("/{id:[0-9]+}")
    @ApiOperation(value = "Delete a Taxi from the database")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The commodity has been successfully deleted"),
            @ApiResponse(code = 404, message = "Taxi with id not found"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response deleteTaxi(
            @ApiParam(value = "Id of Taxi to be deleted", allowableValues = "range[0, infinity]", required = true)
            @PathParam("id")
            long id) {
        Response.ResponseBuilder builder;
        Taxi taxi = service.findById(id);
        if (taxi == null) {
            // Verify that the commodity exists. Return 404, if not present.
            throw new RestServiceException("No Taxi with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        try {
            service.delete(taxi);
            builder = Response.noContent();
        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }
        log.info("deleteTaxi completed. Taxi = " + taxi);
        return builder.build();
    }

    @GET
    @Path("/type/{type}")
    @ApiOperation(value = "Find Commodities by type", notes = "Returns a JSON array of all stored Taxi objects.")
    public Response retrieveCommoditiesByType(@PathParam("type") TaxiStatus type) {
        try {
            List<Taxi> commodities = service.findByType(type);
            return Response.ok(commodities).build();
        }catch (Exception e) {
            throw new RestServiceException(e);
        }
    }
}
