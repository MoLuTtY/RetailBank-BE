package com.cts.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
		
	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<String> accountNotFound(AccountNotFoundException ex) {
		ResponseEntity<String> res = new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
		return res;
	}
	
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<String> customerNotFound(CustomerNotFoundException ex) {
		ResponseEntity<String> res = new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
		return res;
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> accessDenied(AccessDeniedException ex) {
		ResponseEntity<String> res = new ResponseEntity<>(ex.getMessage(),HttpStatus.FORBIDDEN);
		return res;
	}
	
}
