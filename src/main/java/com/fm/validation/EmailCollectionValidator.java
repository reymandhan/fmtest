package com.fm.validation;

import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

import com.fm.annotation.EmailCollection;

public class EmailCollectionValidator implements ConstraintValidator<EmailCollection, Collection<String>>{

	@Override
	public void initialize(EmailCollection constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(Collection<String> value, ConstraintValidatorContext context) {
        
		EmailValidator validator = new EmailValidator();
		boolean status = true;
        for (String s : value) {            
            if (!validator.isValid(s, context)) {
            	status = false;
            	context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(s+" is invalid email")
                        .addConstraintViolation();
            }
            
        }
        return status;
	}

}
