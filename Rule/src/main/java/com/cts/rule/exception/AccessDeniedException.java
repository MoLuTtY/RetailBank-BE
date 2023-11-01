package com.cts.rule.exception;

public class AccessDeniedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AccessDeniedException() {
		super();
		
	}

	public AccessDeniedException(String message) {
		super(message);
	}

}
