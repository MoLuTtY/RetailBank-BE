package com.cts.rule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> accessDenied(AccessDeniedException ex) {
		ResponseEntity<String> res = new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		return res;
	}
	
	
}
