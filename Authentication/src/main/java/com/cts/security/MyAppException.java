package com.cts.security;

import org.springframework.http.HttpStatus;

public class MyAppException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpStatus status;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public MyAppException(HttpStatus status,String message){
        super(message);
        this.status=status;
    }
}
