package com.jwt.exception;

public class UsernameAlreadyExistsException extends RuntimeException{
public UsernameAlreadyExistsException(String message) {
	   super(String.format("Failed for : %s", message));
}
}
