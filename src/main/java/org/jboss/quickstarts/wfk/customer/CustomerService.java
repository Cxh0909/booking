package org.jboss.quickstarts.wfk.customer;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

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
    
    public Customer create(Customer customer) throws Exception {
        log.info("CustomerService.create() - Creating " + customer.getFirstName() + " " + customer.getLastName());
        
        // Check to make sure the data fits with the parameters in the Customer model and passes validation.
        validator.validateCustomer(customer);
        customer.setId(null);
        // Write the contact to the database.
        return crud.create(customer);
    }
    
    public Customer findById(Long id) {
        return crud.findById(id);
    }
    
	List<Customer> findAllOrderedById() {
		return crud.findAllOrderedById();
	}

    Customer delete(Customer customer) throws Exception {
        log.info("delete() - Deleting " + customer.toString());

        Customer deletedCustomer = null;

        if (customer.getId() != null) {
            deletedCustomer = crud.delete(customer);
        } else {
            log.info("delete() - No ID was found so can't Delete.");
        }

        return deletedCustomer;
    }

}
