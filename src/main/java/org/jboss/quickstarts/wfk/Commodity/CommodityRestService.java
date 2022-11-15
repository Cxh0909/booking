package org.jboss.quickstarts.wfk.Commodity;

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

@Path("/commodities")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/commodities", description = "Operations about commodities")
@Stateless
public class CommodityRestService {
	
	@Inject
    private @Named("logger") Logger log;
    
    @Inject
    private CommodityService service;
	
	@SuppressWarnings("unused")
    @POST
    @ApiOperation(value = "Add a new Commodity to the database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Commodity created successfully."),
            @ApiResponse(code = 400, message = "Invalid Commodity supplied in request body"),
            @ApiResponse(code = 409, message = "Commodity supplied in request body conflicts with an existing Commodity"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response createCommodity(
            @ApiParam(value = "JSON representation of Commodity object to be added to the database", required = true)
            Commodity commodity) {


        if (commodity == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {
            // Go add the new Commodity.
            service.create(commodity);

            // Create a "Resource Created" 201 Response and pass the commodity back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(commodity);


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

        log.info("createCommodity completed. Contact = " + commodity.toString());
        return builder.build();
    }

    @DELETE
    @Path("/{id:[0-9]+}")
    @ApiOperation(value = "Delete a Commodity from the database")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The commodity has been successfully deleted"),
            @ApiResponse(code = 400, message = "Invalid Commodity id supplied"),
            @ApiResponse(code = 404, message = "Commodity with id not found"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response deleteCommodity(
            @ApiParam(value = "Id of Commodity to be deleted", allowableValues = "range[0, infinity]", required = true)
            @PathParam("id")
            long id) {

        Response.ResponseBuilder builder;

        Commodity commodity = service.findById(id);
        if (commodity == null) {
            // Verify that the commodity exists. Return 404, if not present.
            throw new RestServiceException("No Commodity with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        try {
            service.delete(commodity);

            builder = Response.noContent();

        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }
        log.info("deleteCommodity completed. Commodity = " + commodity.toString());
        return builder.build();
    }

	@GET
	@ApiOperation(value = "Fetch all Commoditys", notes = "Returns a JSON array of all stored Commodity objects.")
	public Response retrieveAllCommoditys() {
		// Create an empty collection to contain the intersection of Commodity to be
		// returned
		List<Commodity> commodities = service.findAllOrderedById();
		return Response.ok(commodities).build();
	}

    @GET
    @Path("/type/{type}")
    @ApiOperation(value = "Find Commodities by type", notes = "Returns a JSON array of all stored Commodity objects.")
    public Response retrieveCommoditiesByType(@PathParam("type") CommodityType type) {
        try {
            List<Commodity> commodities = service.findByType(type);
            return Response.ok(commodities).build();
        }catch (Exception e) {
            throw new RestServiceException(e);
        }


    }
}
