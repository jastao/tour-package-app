package com.jt.tours.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.tours.security.domain.ApiResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A custom filter that handles whenever an AuthenticationException occurs and saves the http status code and message into a response object.
 * The response object will be sent to the client as JSON object.
 *
 * Created by Jason Tao on 6/11/2020
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        final String expiredMessage = httpServletRequest != null ?
                httpServletRequest.getAttribute("jwt_error") != null ?
                        httpServletRequest.getAttribute("jwt_error").toString() : "" : "";

        ApiResponse response = ApiResponse.builder()
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .message(expiredMessage != null ? expiredMessage : "Unauthorized access.").build();
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, response);
        out.flush();
    }
}
