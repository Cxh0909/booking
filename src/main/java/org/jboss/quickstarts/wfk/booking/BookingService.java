package org.jboss.quickstarts.wfk.booking;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import org.jboss.quickstarts.wfk.exception.DateFormatException;
import org.jboss.quickstarts.wfk.exception.DateRangeException;
import org.jboss.quickstarts.wfk.taxi.Taxi;
import org.jboss.quickstarts.wfk.taxi.TaxiRepository;
import org.jboss.quickstarts.wfk.taxi.TaxiService;
import org.jboss.quickstarts.wfk.util.RestServiceException;
import org.jboss.quickstarts.wfk.util.TimeUtils;

@Dependent
public class BookingService {
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private BookingValidator validator;

	@Inject
    private BookingRepository bookingRepository;

    @Inject
    private TaxiRepository taxiRepository;

    @Inject
    private BookingValidator bookingValidator;

    public Booking create(Booking booking) throws ConstraintViolationException, DateFormatException,
            DateRangeException, RestServiceException, Exception {
        booking.setId(null);

        bookingValidator.validateBooking(booking);

        log.info("BookingService.create() - Creating " + booking);
        booking.setBookingStatus(BookingStatus.CREATED);
        List<Taxi> taxis = taxiRepository.findAllOrderedById();
        LocalDate now = LocalDate.now();
        Taxi waiting = null;
        for (Taxi taxi : taxis) {
            List<Booking> bookings = bookingRepository.findByTaxiId(taxi.getId());
            long count =  bookings.stream().filter(booking1 -> booking1.getBookingStatus() == BookingStatus.CREATED &&
                    now.isBefore(Objects.requireNonNull(TimeUtils.parse(booking1.getBookingEndDate())))).count();
            if (count == 0) {
                waiting = taxi;
                break;
            }
        }
        if (waiting == null) {
            throw new RestServiceException("can't find a free taxi now", Response.Status.NOT_FOUND);
        }
        booking.setTaxi(Collections.singletonList(waiting));
		// Write the booking to the database.
        return bookingRepository.create(booking);
    }

	Booking update(Booking booking) throws Exception {
		log.info("BookingService.update() - Updating " + booking.getId() + " " + booking.getBookingStatus());

		// Check to make sure the data fits with the parameters in the Booking model and
		// passes validation.
		validator.validateBooking(booking);

		// Either update the booking or add it if it can't be found.
		return bookingRepository.update(booking);
	}

    public Booking cancelById(Long id) throws RestServiceException, Exception {
        log.info("BookingService.cancelById() - Canceling " + id);
        Booking booking = bookingRepository.findById(id);

        if (booking == null) {
            // Verify that the commodity exists. Return 404, if not present.
            throw new RestServiceException("No Booking with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }
        booking.setId(id);
        booking.setBookingStatus(BookingStatus.CANCELED);
        return bookingRepository.update(booking);
    }

	public Booking findById(Long id) {
		return bookingRepository.findById(id);
	}

	List<Booking> findAllOrderedById() {
		return bookingRepository.findAllOrderedById();
	}

}
