package com.jt.tours.web.rest.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Central point for exception handling for the entire application. Response entity with the proper error message
 * will be returned.
 *
 * Created by Jason Tao on 6/10/2020
 */
@RestControllerAdvice
public class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler {

    @Bean
    public ErrorAttributes errorAttributes() {
        // Hide exception field in the return object
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
                errorAttributes.remove("exception");
                return errorAttributes;
            }
        };
    }

    /**
     * Handles any HttpServerErrorException and return appropriate status code and the message from the exception.
     *
     * @param ex the exception
     * @return the response entity
     */
    @ExceptionHandler(value = HttpServerErrorException.class)
    public ResponseEntity<Object> handleHttpServerException(HttpServerErrorException ex) {
        return errorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    /**
     * Handles any NoSuchElementException and return appropriate status code and the message from the exception.
     *
     * @param ex the exception
     * @return the response entity
     */
    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        return errorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles any UsernameNotFoundException and return appropriate status code and the message from the exception.
     *
     * @param ex the exception
     * @return the response entity
     */
    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return errorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles any HttpServerErrorException and return appropriate status code and the message from the exception.
     *
     * @param ex the exception
     * @return the response entity
     */
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(ExpiredJwtException ex) {
        return errorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed.");
    }

    /**
     * Handles any AccessDeniedException and return appropriate status code and the message from the exception.
     *
     * @param ex the exception
     * @return the response entity
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return errorResponse(HttpStatus.FORBIDDEN, "Access denied.");
    }

    /**
     * Handles any CustomException and return appropriate status code and the message from the exception.
     *
     * @param ex the exception
     * @return the response entity
     */
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        return errorResponse(ex.getHttpStatus(), ex.getMessage());
    }

    /**
     * Handles any general exception that is not specified.
     *
     * @param ex the exception
     * @return the response entity
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server cannot process the request");
    }

    /**
     * Return the response entity with specified http status code and the message to go along
     *
     * @param status http status code
     * @param message the message to be sent
     * @return the response entity that encapsulates the message and status.
     */
    private ResponseEntity<Object> errorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(message);
    }
}
