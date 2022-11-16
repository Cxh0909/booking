package org.jboss.quickstarts.wfk.booking;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

public class BookingRepository {
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private EntityManager em;

    Booking create(Booking booking) throws Exception {
        log.info("BookingRepository.create() - Creating " + booking.toString());

        // Write the booking to the database.
        em.persist(booking);

        return booking;
    }

	List<Booking> findAllOrderedById() {
		log.info("BookingRepository.findAllOrderedById()");
		TypedQuery<Booking> query = em.createNamedQuery(Booking.FIND_ALL, Booking.class);
		return query.getResultList();
	}

	Booking update(Booking booking) throws Exception {
		log.info("BookingRepository.update() - Updating " + booking.getId());
		// Either update the booking or add it if it can't be found.
		em.merge(booking);
		return booking;
	}

	public Booking findById(Long id) {
		return em.find(Booking.class, id);
	}

	public List<Booking> findByTaxiId(Long taxiId) {
		log.info("BookingRepository.findByTaxiId() - taxiId " + taxiId);
		TypedQuery<Booking> query = em.createNamedQuery(Booking.FIND_BY_TAXI_ID, Booking.class).setParameter("taxiId"
				, taxiId);
		return query.getResultList();
	}
}
