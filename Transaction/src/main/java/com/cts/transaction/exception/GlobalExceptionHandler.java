package com.cts.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
		
	@ExceptionHandler(TransactionNotPossibleException.class)
	public ResponseEntity<String> transferNotPossible(TransactionNotPossibleException ex) {
		ResponseEntity<String> res = new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		return res;
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> accessDenied(AccessDeniedException ex) {
		ResponseEntity<String> res = new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		return res;
	}
	
	
}
