package com.jt.tours.security.filters;

import com.jt.tours.security.CsrUserDetailsService;
import com.jt.tours.web.rest.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

/**
 * Filter for the JWT Authentication and Authorization.
 *
 * Created by Jason Tao on 6/10/2020
 */
@Slf4j
public class JwtTokenFilter extends GenericFilterBean {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer";

    private CsrUserDetailsService csrUserDetailsService;

    public JwtTokenFilter(CsrUserDetailsService csrUserDetailsService) {
        this.csrUserDetailsService = csrUserDetailsService;
    }

    /**
     * Determine if the JWT token is in the HTTP request header.
     * If the token is valid, set the current security context with the Authentication of username and authority groups found in the token.
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        log.info("Process request to check for a JWT token");
        //Extract the authorization head from the http request.
        String headerValue = servletRequest != null ? ((HttpServletRequest) servletRequest).getHeader(AUTHORIZATION_HEADER) : null;

        //Remove the bearer from the header string
        getBearerToken(headerValue).ifPresent(token -> {
            // Extract the username and the authority groups from the token.
            // Since the token is a valid JWT with a trusted user credential,
            // there is no need to retrieve the user from the database.
            try {
                Optional<UserDetails> userDetails = csrUserDetailsService.loadUserByJwtToken(token);

                if(userDetails.isPresent()) {

                    // Add the user details to the security context for this API invocation.
                    SecurityContextHolder.getContext().setAuthentication(
                            new PreAuthenticatedAuthenticationToken(userDetails.get(), "", userDetails.get().getAuthorities()));
                } else {
                    // if the user detail is not available, clear out the context.
                    SecurityContextHolder.clearContext();
                }
            } catch (CustomException ex) {

                // Something went wrong. Clear the context.
                SecurityContextHolder.clearContext();

                // Utilize the attribute to pass the exception message to the security filter. There should be a better approach to
                // handle some fine-grained exceptions other than the two exceptions such as AuthenticationException and AccessDeniedException.
                servletRequest.setAttribute("jwt_error", ex.getMessage().toString());
            }

        });

        // proceed to the next filter in the filter chain
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * If the header is present, extract the Jwt token string from the "Bearer <JWT>" header value.
     *
     * @param headerValue
     * @return jwt token string if present, empty otherwise.
     */
    private Optional<String> getBearerToken(String headerValue) {

        // Sample header value: Authorization: Bearer <JWT Token>
        if(headerValue != null && headerValue.startsWith(BEARER)) {
            return Optional.of(headerValue.replace(BEARER, "").trim());
        }
        return Optional.empty();
    }
}
