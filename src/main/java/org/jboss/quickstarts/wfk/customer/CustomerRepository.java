package org.jboss.quickstarts.wfk.customer;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.quickstarts.wfk.contact.Contact;

public class CustomerRepository {
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private EntityManager em;

    Customer create(Customer customer) throws ConstraintViolationException, ValidationException, Exception {
        log.info("CustomerRepository.create() - Creating " + customer.getFirstName() + " " + customer.getLastName());

        // Write the contact to the database.
        em.persist(customer);

        return customer;
    }
}
