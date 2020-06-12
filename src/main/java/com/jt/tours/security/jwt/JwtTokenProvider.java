package com.jt.tours.security.jwt;

import com.jt.tours.security.domain.AuthUserGroup;
import com.jt.tours.web.rest.exceptions.CustomException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for Jwt operations.
 *
 * Created by Jason Tao on 6/9/2020
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTH_GROUP_KEY = "AUTH_GROUP";
    private static final String ROLE_PREFIX = "ROLE_";

    // The secret key for the jwt
    private String secretKey;

    // The expiration time for the jwt
    private long validityInMilliseconds;

    /**
     * The current approach is to pass in the secret key as environment variable. A better approach would be
     * to store the key in a config server.
     */
    public JwtTokenProvider(@Value("${security.jwt.token.secret-key})") String secretKey,
                            @Value("${security.jwt.token.expiration}") long validityInMilliseconds) {
        // encode the secret key in base64
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }

    /**
     * Create the JWT token with the username and the list of auth user groups
     *
     * @param username the username for the user
     * @param authUserGroups the authority groups belong to the user
     * @return the JWT token string
     */
    public String createToken(String username, Set<AuthUserGroup> authUserGroups) {

        //Add user name to the claim
        Claims claims = Jwts.claims().setSubject(username);

        claims.put(AUTH_GROUP_KEY, authUserGroups.stream()
                .map(authUserGroup -> new SimpleGrantedAuthority(ROLE_PREFIX + authUserGroup.getAuthGroup().getLabel()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));

        // Define the token issue date which is now.
        Date issueDate = new Date();
        Date expiredDate = new Date(issueDate.getTime() + validityInMilliseconds);

        log.debug("JWT token issued at {} and will be expired at {}", issueDate, expiredDate);

        return Jwts.builder()
                    .setClaims(claims)
                    // Define the token issue date which is now.
                    .setIssuedAt(issueDate)
                    // set the expiration date of the jwt token
                    .setExpiration(expiredDate)
                    //use HS256 to sign the secret key
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    .compact();
    }

    /**
     * Validate the JWT string
     * @param token the JWT token string
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {

        try {
            Jwts.parser().setSigningKey(secretKey).parse(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("Jwt token has expired.");
            throw new CustomException(HttpStatus.UNAUTHORIZED, "JWT token has expired.");
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("Error parsing the Jwt token: ", ex);
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid JWT token.");
        }
    }

    /**
     * Get the username from the token string
     * @param token the JWT token string
     * @return the user name
     */
    public String getUserName(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Get the list of authority groups from the token string
     * @param token the JWT token string
     * @return set of authority groups
     */
    public Set<GrantedAuthority> getAuthGroups(String token) {

        // Parse the list of auth user group claims from the token
        List<Map<String, String>> authUserGroupClaims = Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token).getBody().get(AUTH_GROUP_KEY, List.class);

         return authUserGroupClaims.stream()
                                    .map(authUserGroupClaim ->
                                            new SimpleGrantedAuthority(authUserGroupClaim.get("authority")))
                                    .collect(Collectors.toSet());
    }
}
