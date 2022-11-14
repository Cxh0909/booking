package org.jboss.quickstarts.wfk.booking;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.quickstarts.wfk.contact.Contact;

public class BookingRepository {
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private EntityManager em;

    Booking create(Booking booking) throws ConstraintViolationException, ValidationException, Exception {
        log.info("BookingRepository.create() - Creating ");

        // Write the booking to the database.
        em.persist(booking);

        return booking;
    }
}
