package org.jboss.quickstarts.wfk.customer;

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
public class CustomerService {
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private CustomerValidator validator;
	
	@Inject
    private CustomerRepository crud;

    private ResteasyClient client;
    
    public CustomerService() {
        // Create client service instance to make REST requests to upstream service
        client = new ResteasyClientBuilder().build();
    }
    
    Customer create(Customer customer) throws ConstraintViolationException, ValidationException, Exception {
        log.info("CustomerService.create() - Creating " + customer.getFirstName() + " " + customer.getLastName());
        
        // Check to make sure the data fits with the parameters in the Customer model and passes validation.
        validator.validateCustomer(customer);

        // Write the contact to the database.
        return crud.create(customer);
    }

}
