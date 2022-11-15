package org.jboss.quickstarts.wfk.Commodity;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import org.jboss.quickstarts.wfk.customer.Customer;

public class CommodityRepository {
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private EntityManager em;

    Commodity create(Commodity commodity) throws ConstraintViolationException, ValidationException, Exception {
        log.info("CommodityRepository.create() - Creating ");

        // Write the commodity to the database.
        em.persist(commodity);

        return commodity;
    }

    Commodity delete(Commodity commodity) {
        log.info("CommodityRepository.delete() - Deleting " + commodity.getId());
        commodity = em.merge(commodity);
        if (commodity.getId() != null) {
            em.remove(commodity);
            em.flush();
        } else {
            log.info("CustomerRepository.delete() - No ID was found so can't Delete.");
        }

        return commodity;
    }

	List<Commodity> findAllOrderedById() {
		TypedQuery<Commodity> query = em.createNamedQuery(Commodity.FIND_ALL, Commodity.class);
		return query.getResultList();
	}

    List<Commodity> findByType(String type) {
        TypedQuery<Commodity> query = em.createNamedQuery(Commodity.FIND_BY_TYPE, Commodity.class).setParameter("type", type);
        return query.getResultList();
    }

    public Commodity findById(Long id) {
        return em.find(Commodity.class, id);
    }
}
