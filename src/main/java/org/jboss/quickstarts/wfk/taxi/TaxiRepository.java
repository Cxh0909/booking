package org.jboss.quickstarts.wfk.taxi;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

public class TaxiRepository {
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private EntityManager em;

    Taxi create(Taxi taxi) throws ConstraintViolationException, ValidationException, Exception {
        log.info("TaxiRepository.create() - Creating ");

        // Write the commodity to the database.
        em.persist(taxi);

        return taxi;
    }

    Taxi delete(Taxi taxi) {
        log.info("TaxiRepository.delete() - Deleting " + taxi.getId());
        taxi = em.merge(taxi);
        if (taxi.getId() != null) {
            em.remove(taxi);
            em.flush();
        } else {
            log.info("CustomerRepository.delete() - No ID was found so can't Delete.");
        }

        return taxi;
    }

	List<Taxi> findAllOrderedById() {
		TypedQuery<Taxi> query = em.createNamedQuery(Taxi.FIND_ALL, Taxi.class);
		return query.getResultList();
	}

    List<Taxi> findByType(String status) {
        TypedQuery<Taxi> query = em.createNamedQuery(Taxi.FIND_BY_TYPE, Taxi.class).setParameter("status", status);
        return query.getResultList();
    }

    public Taxi findById(Long id) {
        return em.find(Taxi.class, id);
    }
}
