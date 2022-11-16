package org.jboss.quickstarts.wfk.booking;

import static org.jboss.quickstarts.wfk.util.TimeUtils.parse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.jboss.quickstarts.wfk.exception.DateFormatException;
import org.jboss.quickstarts.wfk.exception.DateRangeException;

public class BookingValidator {

	@Inject
    private Validator validator;
    
    void validateBooking(Booking booking) throws ConstraintViolationException, DateFormatException, DateRangeException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        if (booking.getCustomer() == null || booking.getCustomer().getId() == null) {
            throw new NullPointerException("customer's id can't be null");
        }

        LocalDate bookingStartDate = parse(booking.getBookingStartDate());
        LocalDate bookingEndDate = parse(booking.getBookingEndDate());
        if (bookingStartDate == null || bookingEndDate == null) {
            throw new DateFormatException("Date Format Violation");
        }

        if (bookingEndDate.isBefore(bookingStartDate)) {
            throw new DateRangeException("Date Range Violation");
        }
    }
}
