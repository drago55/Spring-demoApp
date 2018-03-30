package com.drago.spring.demo.exception;

import java.io.IOException;

public class StorageException extends RuntimeException {
    public StorageException(String msg, IOException e) {
    }

    public StorageException(String s) {
    }
}
