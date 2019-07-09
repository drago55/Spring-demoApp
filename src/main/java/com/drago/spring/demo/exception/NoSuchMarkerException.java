package com.drago.spring.demo.exception;

public class NoSuchMarkerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1175647012359015792L;

	public NoSuchMarkerException() {
	}

	public NoSuchMarkerException(String message) {
		super(message);
	}

	public NoSuchMarkerException(String message, Throwable cause) {
		super(message, cause);
	}
}
