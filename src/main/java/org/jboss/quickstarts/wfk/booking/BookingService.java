package org.jboss.quickstarts.wfk.booking;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.quickstarts.wfk.taxi.Taxi;
import org.jboss.quickstarts.wfk.taxi.TaxiService;
import org.jboss.quickstarts.wfk.taxi.TaxiStatus;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

@Dependent
public class BookingService {
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private BookingValidator validator;

    @Inject
    private TaxiService taxiService;

	@Inject
    private BookingRepository crud;

    private ResteasyClient client;
    
    public BookingService() {
        // Create client service instance to make REST requests to upstream service
        client = new ResteasyClientBuilder().build();
    }
    
    public Booking create(Booking booking) throws Exception {
        log.info("TaxiService.create() - Creating " + booking.toString());
        booking.setBookingStatus(BookingStatus.CREATED);
        Taxi taxi = taxiService.pickAFreeTaxi();
        taxi.setTaxiType(TaxiStatus.BUSY);
        booking.setTaxi(taxi);
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
