package com.drago.spring.demo.exception;

public class UserNotExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8310060787511058120L;

	public UserNotExistException() {
	}

	public UserNotExistException(String message) {
		super(message);
	}

	public UserNotExistException(String message, Throwable cause) {
		super(message, cause);
	}
}
