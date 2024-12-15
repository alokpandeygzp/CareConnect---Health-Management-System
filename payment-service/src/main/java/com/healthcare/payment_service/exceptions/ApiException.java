package com.healthcare.payment_service.exceptions;

public class ApiException extends RuntimeException{

    public ApiException(String message) {
        super(message);
    }

    public ApiException() {
        super();
    }
}
