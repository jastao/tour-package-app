package com.jt.tours.security.domain;

import lombok.Builder;
import lombok.Data;

/**
 * Response object that wraps the http status code, message and result and sends back to the client.
 *
 * Created by Jason Tao on 6/11/2020
 */
@Data
public class ApiResponse {

    private int status;
    private String message;
    private Object result;

    @Builder
    public ApiResponse(int status, String message, Object result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    @Builder
    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
