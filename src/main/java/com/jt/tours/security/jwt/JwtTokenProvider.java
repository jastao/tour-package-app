package com.jt.tours.security.jwt;

import com.jt.tours.security.domain.AuthUserGroup;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for Jwt operations.
 *
 * Created by Jason Tao on 6/9/2020
 */
@Slf4j
@Component
public class JwtTokenProvider implements Serializable {

    private static final String AUTH_GROUP_KEY = "AUTH_GROUP";
    private static final String ROLE_PREFIX = "ROLE_";

    // The expiration time for the jwt
    private static final long JWT_VALIDITY_IN_MILLISECONDS = 5 * 60 * 60 * 1000;

    // The secret key for the jwt
    private String secretKey;

    /**
     * The current approach is to pass in the secret key as environment variable. A better approach would be
     * to store the key in a config server.
     */
    @Autowired
    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secretKey) {
        // encode the secret key in base64
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * Create the JWT token with the username and the list of auth user groups
     *
     * @param username the username for the user
     * @param authUserGroups the authority groups belong to the user
     * @return the JWT token string
     */
    public String createToken(String username, Set<AuthUserGroup> authUserGroups) {

        // Add user name to the claim
        Claims claims = Jwts.claims().setSubject(username);

        // Add a set of allowed auth group to the claims.
        claims.put(AUTH_GROUP_KEY,
                authUserGroups.stream()
                .map(authUserGroup -> new SimpleGrantedAuthority(ROLE_PREFIX + authUserGroup.getAuthGroup().getLabel()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));

        // Define the token issue date which is now.
        Date issueDate = new Date();
        Date expiredDate = new Date(issueDate.getTime() + JWT_VALIDITY_IN_MILLISECONDS);

        log.debug("JWT token issued at {} and will be expired at {}", issueDate, expiredDate);

        return Jwts.builder()
                    .setClaims(claims)
                    // Define the token issue date which is now.
                    .setIssuedAt(issueDate)
                    // set the expiration date of the jwt token
                    .setExpiration(expiredDate)
                    //use HS256 to sign the secret key
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    // compacting the JWT to a URL-safe string
                    .compact();
    }

    /**
     * Validate the JWT string
     * @param token the JWT token string
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        final String username = getUserNameFromToken(token);
        return (username != null && !getExpiredDateFromToken(token).before(new Date()));
    }

    /**
     * Get the username from the claim extracted from the token string
     * @param token the JWT token string
     * @return the user name
     */
    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Get the expiration date from the claim extracted from the token string
     *
     * @param token the JWT token string
     * @return the expiration date
     */
    public Date getExpiredDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Get the list of authority groups from the token string
     * @param token the JWT token string
     * @return set of authority groups
     */
    public Set<GrantedAuthority> getAuthGroupsFromToken(String token) {

        // Parse the list of auth user group claims from the token
        List<Map<String, String>> authUserGroupClaims = Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(token).getBody().get(AUTH_GROUP_KEY, List.class);

         return authUserGroupClaims.stream()
                                    .map(authUserGroupClaim ->
                                            new SimpleGrantedAuthority(authUserGroupClaim.get("authority")))
                                    .collect(Collectors.toSet());
    }

    /**
     * Get a specified claim from the token.
     *
     * @param token the JWT token string
     * @param claimsResolver the function interface used for extracting the claim
     * @param <T> the generic type to be returned
     * @return the claim
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Get all the claims by parsing the token using the secret key.
     *
     * @param token the JWT token string
     * @return claims object extracted from the token.
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}
