package com.cts.account.exception;

public class MinimumBalanceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MinimumBalanceException() {
		
	}

	public MinimumBalanceException(String message) {
		super(message);
		
	}


}
