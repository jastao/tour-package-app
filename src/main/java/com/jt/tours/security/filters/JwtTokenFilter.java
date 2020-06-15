package com.jt.tours.security.filters;

import com.jt.tours.security.CsrUserDetailsService;
import com.jt.tours.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Filter for the JWT Authentication and Authorization.
 * <p>
 * Created by Jason Tao on 6/10/2020
 */
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer";

    private CsrUserDetailsService csrUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(CsrUserDetailsService csrUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.csrUserDetailsService = csrUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Determine if the JWT token is in the HTTP request header.
     * If the token is valid, set the current security context with the Authentication of username and authority groups found in the token.
     *
     * @param httpServletRequest http request
     * @param httpServletResponse http response
     * @param filterChain the security filter chain
     * @throws IOException any IO exceptions
     * @throws ServletException any servlet exceptions
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        //Extract the authorization head from the http request.
        final String reqTokenHeader = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

        //Remove the bearer from the header string
        log.info("Process request to check for a JWT token");
        getBearerToken(reqTokenHeader).ifPresentOrElse(token -> {
            processAuthentication(token, httpServletRequest);
        }, () -> log.info("JWT token does not exist in the authorization header."));

        // proceed to the next filter in the filter chain
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * If the header is present, extract the Jwt token string from the "Bearer <JWT>" header value.
     *
     * @param reqTokenHeader the header value in the http request that contains the authorization header.
     * @return jwt token string if present, empty otherwise.
     */
    private Optional<String> getBearerToken(String reqTokenHeader) {

        // Sample header value: Authorization: Bearer <JWT Token>
        if (reqTokenHeader != null && reqTokenHeader.startsWith(BEARER)) {
            return Optional.of(reqTokenHeader.replace(BEARER, "").trim());
        }
        return Optional.empty();
    }

    /**
     * Perform authentication from the incoming token.
     *
     * @param token the JWT token string
     * @param httpServletRequest the servlet request used for setting the jwt error attribute.
     */
    private void processAuthentication(String token, HttpServletRequest httpServletRequest) {
        String username = getUserNameFromToken(token, httpServletRequest);
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();

        //If the username is not null and there is no existing authentication object
        if (username != null && principal == null) {

                // Validate the token
                if (jwtTokenProvider.validateToken(token)) {

                    csrUserDetailsService.loadUserByJwtToken(token).ifPresent( userDetails ->

                            // Add the user details to the security context for this API invocation once validation completes.
                            SecurityContextHolder.getContext().setAuthentication(
                                    new PreAuthenticatedAuthenticationToken(userDetails, "", userDetails.getAuthorities()))
                    );

                }
        }
    }

    /**
     * Validates the token. If there is anything wrong with the jwt, set the error to an attribute in the
     * http request for error handling. Clear the security context.
     *
     * @param token the JWT token string
     * @param httpServletRequest the servlet request used for setting the jwt error attribute.
     * @return the username
     */
    private String getUserNameFromToken(String token, HttpServletRequest httpServletRequest) {
        String username = null;
        try {
            username = jwtTokenProvider.getUserNameFromToken(token);
        } catch (ExpiredJwtException ex) {
            log.error("JWT Token has expired.");
            httpServletRequest.setAttribute("jwt_error", ex.getMessage());
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("Unable to parse the JWT token.");
            httpServletRequest.setAttribute("jwt_error", ex.getMessage());
        } finally {
            // Something went wrong. Clear the context.
            SecurityContextHolder.clearContext();
        }
        return username;
    }
}
