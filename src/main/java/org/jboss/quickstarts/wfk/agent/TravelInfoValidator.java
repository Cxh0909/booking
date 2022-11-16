package org.jboss.quickstarts.wfk.agent;

import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.exception.DateFormatException;
import org.jboss.quickstarts.wfk.exception.DateRangeException;

/**
 * @author yu zhang
 */
public class TravelInfoValidator {
    @Inject
    private Validator validator;

    void validateTravelInfo(TravelInfo travelInfo) throws ConstraintViolationException, DateFormatException,
            DateRangeException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<TravelInfo>> violations = validator.validate(travelInfo);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        if (travelInfo.getCommodityType() == CommodityType.FLIGHT) {
            if (travelInfo.getFlightBooking() == null || travelInfo.getFlightBooking().getCustomer() == null
                    || travelInfo.getFlightBooking().getCustomer().getId() == null) {
                throw new IllegalArgumentException("illegal argument");
            }
        }
        if (travelInfo.getCommodityType() == CommodityType.HOTEL) {
            if (travelInfo.getHotelBooking() == null || travelInfo.getHotelBooking().getCustomerId() == null) {
                throw new IllegalArgumentException("illegal argument");
            }
        }
    }
}
