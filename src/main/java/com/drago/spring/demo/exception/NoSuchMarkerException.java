package com.drago.spring.demo.exception;

public class NoSuchMarkerException extends RuntimeException{

    public NoSuchMarkerException() {
    }

    public NoSuchMarkerException(String message) {
        super(message);
    }

    public NoSuchMarkerException(String message, Throwable cause) {
        super(message, cause);
    }
}
