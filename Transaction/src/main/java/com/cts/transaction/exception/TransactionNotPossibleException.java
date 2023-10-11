package com.cts.transaction.exception;

public class TransactionNotPossibleException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TransactionNotPossibleException() {
		super();
		
	}

	public TransactionNotPossibleException(String message) {
		super(message);
		
	}


}
