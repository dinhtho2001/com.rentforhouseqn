package com.rentforhouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice 
public class ControllerAdvisor {
	@ExceptionHandler(MyFileNotFoundException.class)
	public ResponseEntity<Object> handleCityNotFoundException(
			MyFileNotFoundException ex, WebRequest request) {
			
		ErrorResponse sysError = new ErrorResponse();
		sysError.setError(ex.getMessage());
		
	    return new ResponseEntity<>(sysError, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
