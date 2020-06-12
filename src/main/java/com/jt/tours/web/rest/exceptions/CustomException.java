package com.jt.tours.web.rest.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Generic custom exception that extends the RuntimeException class.
 *
 * Created by Jason Tao on 6/11/2020
 */
public class CustomException extends RuntimeException {

    private final String message;

    private final HttpStatus httpStatus;

    public CustomException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
