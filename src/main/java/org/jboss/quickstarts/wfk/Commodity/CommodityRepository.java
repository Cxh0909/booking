package org.jboss.quickstarts.wfk.Commodity;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.quickstarts.wfk.contact.Contact;

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
}
