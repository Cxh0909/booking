package org.jboss.quickstarts.wfk.booking;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.quickstarts.wfk.Commodity.Commodity;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

@Dependent
public class BookingService {
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private BookingValidator validator;
	
	@Inject
    private BookingRepository crud;

    private ResteasyClient client;
    
    public BookingService() {
        // Create client service instance to make REST requests to upstream service
        client = new ResteasyClientBuilder().build();
    }
    
    Booking create(Long customerId, Long commodityId) throws Exception {
        log.info("CommodityService.create() - Creating customerId = " + customerId + "commodityId = " + commodityId);
        
        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.CREATED);

        Customer customer = new Customer();
        customer.setId(customerId);
        booking.setCustomer(customer);

        Commodity commodity = new Commodity();
        commodity.setId(commodityId);
        booking.setCommodity(commodity);

		// Write the booking to the database.
        return crud.create(booking);
    }

	Booking update(Booking booking) throws Exception {
		log.info("BookingService.update() - Updating " + booking.getId() + " " + booking.getBookingStatus());

		// Check to make sure the data fits with the parameters in the Booking model and
		// passes validation.
		validator.validateBooking(booking);

		// Either update the booking or add it if it can't be found.
		return crud.update(booking);
	}

    public Booking cancelById(Long id) throws Exception {
        log.info("BookingService.cancelById() - Canceling " + id);
        Booking booking = new Booking();
        booking.setId(id);
        booking.setBookingStatus(BookingStatus.CANCELED);
        return crud.update(booking);
    }

	public Booking findById(Long id) {
		return crud.findById(id);
	}

	List<Booking> findAllOrderedById() {
		return crud.findAllOrderedById();
	}

}
