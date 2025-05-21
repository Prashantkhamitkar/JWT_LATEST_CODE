package com.jwt.exception;

public class EmailAlreadyExistsException extends RuntimeException {
	
	public EmailAlreadyExistsException(String message) {
		   super(String.format("Failed for : %s", message));
	}

}
