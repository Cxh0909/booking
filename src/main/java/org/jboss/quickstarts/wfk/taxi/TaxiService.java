package org.jboss.quickstarts.wfk.taxi;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

@Dependent
public class TaxiService {
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private TaxiValidator validator;
	
	@Inject
    private TaxiRepository crud;

    private ResteasyClient client;
    
    public TaxiService() {
        // Create client service instance to make REST requests to upstream service
        client = new ResteasyClientBuilder().build();
    }
    
    Taxi create(Taxi taxi) throws ConstraintViolationException, ValidationException, Exception {
        log.info("TaxiService.create() - Creating " + taxi.getId() + " " + taxi.getTaxiType());
        
        // Check to make sure the data fits with the parameters in the Taxi model and passes validation.
        validator.validateTaxi(taxi);
        taxi.setId(null);
        // Write the contact to the database.
        return crud.create(taxi);
    }

    Taxi delete(Taxi taxi) {
        log.info("TaxiService.delete() - Deleting " + taxi.toString());
        return crud.delete(taxi);
    }

	List<Taxi> findAllOrderedById() {
		return crud.findAllOrderedById();
	}

    public Taxi pickAFreeTaxi() {
        List<Taxi> taxis = crud.findByType(TaxiStatus.FREE.name());
        if (taxis == null || taxis.isEmpty()) {
            return null;
        }
        return taxis.get(0);
    }

    public List<Taxi> findByType(TaxiStatus taxiStatus) {
        return crud.findByType(taxiStatus.name());
    }

    Taxi findById(Long id) {
        return crud.findById(id);
    }
}
