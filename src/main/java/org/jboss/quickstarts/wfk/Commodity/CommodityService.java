package org.jboss.quickstarts.wfk.Commodity;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

@Dependent
public class CommodityService {
	@Inject
    private @Named("logger") Logger log;

    @Inject
    private CommodityValidator validator;
	
	@Inject
    private CommodityRepository crud;

    private ResteasyClient client;
    
    public CommodityService() {
        // Create client service instance to make REST requests to upstream service
        client = new ResteasyClientBuilder().build();
    }
    
    Commodity create(Commodity commodity) throws ConstraintViolationException, ValidationException, Exception {
        log.info("CommodityService.create() - Creating " + commodity.getId() + " " + commodity.getCommodityType());
        
        // Check to make sure the data fits with the parameters in the Commodity model and passes validation.
        validator.validateCommodity(commodity);

        // Write the contact to the database.
        return crud.create(commodity);
    }

    Commodity delete(Commodity commodity) {
        log.info("CommodityService.delete() - Deleting " + commodity.toString());
        return crud.delete(commodity);
    }

	List<Commodity> findAllOrderedById() {
		return crud.findAllOrderedById();
	}

    List<Commodity> findByType(CommodityType type) {
        return crud.findByType(type.name());
    }

    Commodity findById(Long id) {
        return crud.findById(id);
    }
}
