package org.jboss.quickstarts.wfk.customer;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;

import org.jboss.quickstarts.wfk.exception.UniqueEmailException;
import org.jboss.quickstarts.wfk.exception.UniquePhoneNumberException;

@Dependent
public class CustomerService {
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private CustomerValidator validator;
	
	@Inject
    private CustomerRepository crud;
    
    public Customer create(Customer customer) throws ConstraintViolationException, UniqueEmailException,
            UniquePhoneNumberException, Exception {
        customer.setId(null);
        log.info("CustomerService.create() - Creating " + customer.toString());
        
        // Check to make sure the data fits with the parameters in the Customer model and passes validation.
        validator.validateCustomer(customer);
        // Write the contact to the database.
        return crud.create(customer);
    }
    
    public Customer findById(Long id) {
        return crud.findById(id);
    }
    
	List<Customer> findAllOrderedById() throws Exception {
        log.info("CustomerService.findAllOrderedById()");
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
