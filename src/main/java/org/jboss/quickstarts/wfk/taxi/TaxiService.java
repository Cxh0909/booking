package org.jboss.quickstarts.wfk.taxi;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import javax.ws.rs.core.Response;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.booking.BookingRepository;
import org.jboss.quickstarts.wfk.booking.BookingStatus;
import org.jboss.quickstarts.wfk.util.RestServiceException;
import org.jboss.quickstarts.wfk.util.TimeUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

@Dependent
public class TaxiService {
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private TaxiValidator validator;
	
	@Inject
    private TaxiRepository taxiRepository;

    @Inject
    private BookingRepository bookingRepository;
    
    Taxi create(Taxi taxi) throws ConstraintViolationException, ValidationException, Exception {
        taxi.setId(null);
        log.info("TaxiService.create() - Creating " + taxi);
        
        // Check to make sure the data fits with the parameters in the Taxi model and passes validation.
        validator.validateTaxi(taxi);
        // Write the contact to the database.
        return taxiRepository.create(taxi);
    }

    Taxi delete(Taxi taxi) {
        log.info("TaxiService.delete() - Deleting " + taxi.toString());
        return taxiRepository.delete(taxi);
    }

	public List<Taxi> findAllOrderedById() {
        log.info("TaxiService.findAllOrderedById()");
		return taxiRepository.findAllOrderedById();
	}

    public List<Taxi> findByType(TaxiStatus taxiStatus) {
        return taxiRepository.findByType(taxiStatus.name());
    }

    Taxi findById(Long id) {
        return taxiRepository.findById(id);
    }

    public Taxi pickAFreeTaxi() {
        List<Taxi> taxis = taxiRepository.findAllOrderedById();
        LocalDate now = LocalDate.now();
        Taxi waiting = null;
        for (Taxi taxi : taxis) {
            List<Booking> bookings = bookingRepository.findByTaxiId(taxi.getId());
            long count = bookings.stream().filter(booking1 -> booking1.getBookingStatus() == BookingStatus.CREATED &&
                    now.isBefore(Objects.requireNonNull(TimeUtils.parse(booking1.getBookingEndDate())))).count();
            if (count == 0) {
                waiting = taxi;
                break;
            }
        }
        if (waiting == null) {
            throw new RestServiceException("can't find a free taxi now", Response.Status.NOT_FOUND);
        }
        return waiting;
    }
}
