package com.fm.exception;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fm.bean.CommonResponseBean;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		CommonResponseBean response = new CommonResponseBean(false, ex.getLocalizedMessage(), errors);
		return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		List<String> errors = new ArrayList<String>();

		if (ex.getConstraintViolations() != null) {
			for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
				errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
						+ violation.getMessage());
			}
		}

		CommonResponseBean response = new CommonResponseBean(false, ex.getLocalizedMessage(), errors);
		return new ResponseEntity<Object>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}
}
