package org.jboss.quickstarts.wfk.booking;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

import org.jboss.quickstarts.wfk.area.Area;
import org.jboss.quickstarts.wfk.area.AreaService;
import org.jboss.quickstarts.wfk.area.InvalidAreaCodeException;
import org.jboss.quickstarts.wfk.contact.Contact;
import org.jboss.quickstarts.wfk.contact.ContactRepository;
import org.jboss.quickstarts.wfk.contact.ContactValidator;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

@Dependent
public class BookingService {
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private BookingValidator validator;
	
	@Inject
    private BookingRepository crud;

    private ResteasyClient client;
    
    public BookingService() {
        // Create client service instance to make REST requests to upstream service
        client = new ResteasyClientBuilder().build();
    }
    
    Booking create(Booking booking) throws ConstraintViolationException, ValidationException, Exception {
        log.info("CommodityService.create() - Creating " + booking.getId());
        
        // Check to make sure the data fits with the parameters in the Commodity model and passes validation.
        validator.validateBooking(booking);

        // Write the contact to the database.
        return crud.create(booking);
    }

}
