package com.drago.spring.demo.exception;

public class InvalidUserException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5468553135016520558L;

	public InvalidUserException() {
    }

    public InvalidUserException(String message) {
        super(message);
    }

    public InvalidUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
