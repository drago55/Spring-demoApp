package com.drago.spring.demo.exception;

import java.io.IOException;

public class StorageException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5257050148681794738L;

	public StorageException(String msg, IOException e) {
	}

	public StorageException(String s) {
	}
}
