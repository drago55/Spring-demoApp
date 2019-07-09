package com.drago.spring.demo.exception;

public class EmailExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8143386745141348174L;

	public EmailExistsException(String message) {
		super(message);
	}

	public EmailExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailExistsException() {

	}
}
