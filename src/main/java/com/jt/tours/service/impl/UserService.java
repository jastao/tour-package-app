package com.jt.tours.service.impl;

import com.jt.tours.repository.AuthUserGroupRepository;
import com.jt.tours.repository.UserRepository;
import com.jt.tours.security.domain.AuthUserGroup;
import com.jt.tours.security.domain.AuthUserGroupEnum;
import com.jt.tours.security.domain.User;
import com.jt.tours.security.jwt.JwtTokenProvider;
import com.jt.tours.web.rest.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The User service
 *
 * Created by Jason Tao on 6/9/2020
 */
@Slf4j
@Service
@Transactional
public class UserService {

    private static final String CSR_USER_AUTH_GROUP = "CSR_USER";

    private UserRepository userRepository;
    private AuthUserGroupRepository authUserGroupRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    public UserService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       AuthUserGroupRepository authUserGroupRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.authUserGroupRepository = authUserGroupRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Sign in the user with JWT-enabled authentication
     *
     * @param username
     * @param password
     * @return Optional of the JWT token string, empty otherwise.
     */
    public Optional<String> signin(String username, String password) {

        log.info("User is attempting to sign in.");

        // Create the empty optional for the jwt token
        Optional<String> jwtToken = Optional.empty();

        // Retrieve the user from database
        Optional<User> foundUser = userRepository.findByUsername(username);

        if(foundUser.isPresent()) {

            try {
                // If user exists, authenticate user
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

                // User is valid, generate the JWT token and return it.
                log.info("Generating JWT token for user {}", username);
                jwtToken = Optional.of(jwtTokenProvider.createToken(username, foundUser.get().getAuthUserGroups()));

            } catch (AuthenticationException ex) {
                log.error("Failed to login with user name {}", username);
                throw new CustomException(HttpStatus.FORBIDDEN, "User authentication failed.");
            }
        }
        return jwtToken;
    }

    /**
     * Register the new user. If the username exists, return the user from database. Otherwise, create it.
     *
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param email
     * @return Optional of user, empty if the user already exists.
     */
    public Optional<User> register(String username,
                                   String password,
                                   String firstName,
                                   String lastName,
                                   String email) {

        // Check to see if this username already exists in database
        Optional<User> foundUser = Optional.empty();

        if(userRepository.findByUsername(username).isEmpty()) {

            AuthUserGroup authUserGroup = authUserGroupRepository.findByAuthGroup(AuthUserGroupEnum.findByLabel(CSR_USER_AUTH_GROUP));

            // If user does not exist, persist the new user
            foundUser = Optional.of(
                        userRepository.save(User.builder()
                                .username(username)
                                .password(passwordEncoder.encode(password))
                                .firstName(firstName)
                                .lastName(lastName)
                                .email(email)
                                .authUserGroups(Set.of(authUserGroup))
                                .build()));
            log.info("Register new user {} ", foundUser.get().getUsername());
        }
        return foundUser;
    }

    /**
     * Find specified user with id.
     *
     * @param username
     * @return Optional of User.
     */
    public Optional<User> getUser(String username) {

        log.info("Find user {} exists in database.", username);
        return userRepository.findByUsername(username);
    }

    /**
     * Retrieve all users from database
     *
     * @return list of registered users
     */
    public List<User> getAll() {

        log.info("Find all users.");
        return userRepository.findAll();
    }
}
