package com.jt.tours.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.tours.security.domain.ApiResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A custom filter that handles whenever an AccessDeniedException occurs and saves the http status code and message into a response object.
 * The response object will be sent to the client as JSON object.
 *
 * Created by Jason Tao on 6/11/2020
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {

        ApiResponse response = ApiResponse.builder()
                                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                                    .message("Access Denied").build();
        OutputStream os = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(os, response);
        os.flush();
    }
}
