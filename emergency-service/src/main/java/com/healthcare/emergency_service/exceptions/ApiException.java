package com.healthcare.emergency_service.exceptions;

public class ApiException extends RuntimeException{

    public ApiException(String message) {
        super(message);
    }

    public ApiException() {
        super();
    }
}
