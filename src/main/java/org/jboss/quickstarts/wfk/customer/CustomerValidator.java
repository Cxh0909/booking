package org.jboss.quickstarts.wfk.customer;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.jboss.quickstarts.wfk.exception.UniqueEmailException;
import org.jboss.quickstarts.wfk.exception.UniquePhoneNumberException;

public class CustomerValidator {
	@Inject
    private Validator validator;

    @Inject
    private CustomerRepository crud;
    
    void validateCustomer(Customer customer) throws ConstraintViolationException, UniqueEmailException, UniquePhoneNumberException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(customer.getEmail())) {
            throw new UniqueEmailException("Unique Email Violation");
        }

        if (phoneNumberAlreadyExists(customer.getPhoneNumber())) {
            throw new UniquePhoneNumberException("Unique PhoneNumber Violation");
        }
    }

    boolean emailAlreadyExists(String email) {
        return Objects.nonNull(crud.findByEmail(email));
    }

    boolean phoneNumberAlreadyExists(String phoneNumber) {
        return Objects.nonNull(crud.findByPhoneNumber(phoneNumber));
    }
}
