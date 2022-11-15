package org.jboss.quickstarts.wfk.customer;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

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
    
    Customer findById(Long id) {
        return em.find(Customer.class, id);
    }
    
	List<Customer> findAllOrderedById() {
		TypedQuery<Customer> query = em.createNamedQuery(Customer.FIND_ALL, Customer.class);
		return query.getResultList();
	}

    Customer delete(Customer customer) throws Exception {
        log.info("CustomerRepository.delete() - Deleting " + customer.getFirstName() + " " + customer.getLastName());

        if (customer.getId() != null) {
            /*
             * The Hibernate session (aka EntityManager's persistent context) is closed and invalidated after the commit(), 
             * because it is bound to a transaction. The object goes into a detached status. If you open a new persistent 
             * context, the object isn't known as in a persistent state in this new context, so you have to merge it. 
             * 
             * Merge sees that the object has a primary key (id), so it knows it is not new and must hit the database 
             * to reattach it. 
             * 
             * Note, there is NO remove method which would just take a primary key (id) and a entity class as argument. 
             * You first need an object in a persistent state to be able to delete it.
             * 
             * Therefore we merge first and then we can remove it.
             */
            em.remove(em.merge(customer));

        } else {
            log.info("CustomerRepository.delete() - No ID was found so can't Delete.");
        }

        return customer;
    }
}
