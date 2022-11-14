package org.jboss.quickstarts.wfk.Commodity;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.jboss.quickstarts.wfk.contact.Contact;
import org.jboss.quickstarts.wfk.contact.ContactRepository;
import org.jboss.quickstarts.wfk.contact.UniqueEmailException;

public class CommodityValidator {
	@Inject
    private Validator validator;

    @Inject
    private CommodityRepository crud;
    
    void validateCommodity(Commodity commodity)throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Commodity>> violations = validator.validate(commodity);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

}
