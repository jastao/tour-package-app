package com.jt.tours.web.rest.controller;

import com.jt.tours.security.domain.User;
import com.jt.tours.service.impl.UserService;
import com.jt.tours.web.rest.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * User controller
 *
 * Created by Jason Tao on 6/9/2020
 */
@RestController
@RequestMapping("${spring.data.rest.base-path}/users")
public class UserController {

    private String ROOT_API_PATH_PREFIX;
    private UserService userService;

    public UserController(UserService userService, @Value("${spring.data.rest.base-path}") String pathPrefix) {
        this.userService = userService;
        this.ROOT_API_PATH_PREFIX = pathPrefix;
    }

    /**
     * Login for the user. If the user credential fails, it throws a HttpServerErrorException which
     * will be converted into a response message to send back to the client.
     *
     * @param userDTO the user credential
     * @return the JWT token wrapped inside the response entity with http status of 200 (ok)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserDTO userDTO) {

        String token = userService.signin(userDTO.getUsername(), userDTO.getPassword()).orElseThrow(() ->
                    new UsernameNotFoundException("User name (" + userDTO.getUsername() + ") does not exist. Failed sign in."));

        // send 200 with JWT token
        return ResponseEntity.ok(token);
    }

    /**
     * Logout for the user. If there is an user authentication, delegates the logout process to the SecurityContextLogoutHandler.
     *
     * @param httpServletRequest
     * @param httpServletResponse
     */
    @GetMapping("/logout")
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        Optional.of(SecurityContextHolder.getContext().getAuthentication()).ifPresent(auth -> {
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
        });
    }

    /**
     * Register for the user. If the user already exists in the system, it throws a HttpServerErrorException which
     * will be converted into a response message to send back to the client.
     * Only administrator authority group can access this method.
     *
     * @param userDTO the user credential
     * @return the User wrapped inside the response entity with http status of 201 (created)
     */
    @PostMapping("/register")
    @PreAuthorize("hasRole('CSR_ADMIN')")
    public ResponseEntity<User> register(@Valid @RequestBody UserDTO userDTO) {

        User newUser = userService.register(userDTO.getUsername(),
                             userDTO.getPassword(),
                             userDTO.getFirstName(),
                             userDTO.getLastName(),
                             userDTO.getEmail()).orElseThrow(() ->
                new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Register failed. User already exists."));

        // send 201 with registered user
        return ResponseEntity.created(
                                UriComponentsBuilder.fromPath(ROOT_API_PATH_PREFIX + "/users/" + newUser.getId()).build().toUri())
                             .body(newUser);
    }

    /**
     * Get a specified user by username
     *
     * @param username
     * @return the User wrapped inside the response entity with http status of 200 (ok)
     */
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('CSR_ADMIN')")
    public ResponseEntity<?> getUser(@PathVariable("username") String username) {

        //Check if this is a valid id
        if(username == null) {
            return ResponseEntity.badRequest().body("The user name cannot be null.");
        }

        // Search for the user using the username. If not found, throw the exception.
        User foundUser = userService.getUser(username).orElseThrow(() ->
                new UsernameNotFoundException("User name (" + username + ") does not exist."));

        // send ok with the found user
        return ResponseEntity.ok(foundUser);
    }

    /**
     * Retrieves the full list of users available. Only administrator authority group can access this method.
     *
     * @return response entity with the list of user as content. If the list is empty,
     */
    @GetMapping
    @PreAuthorize("hasRole('CSR_ADMIN')")
    public ResponseEntity<List<User>> getUser() {

        List<User> users = userService.getAll();
        if(users.isEmpty()) {
            // send no content if the list is empty
            return ResponseEntity.noContent().header("Content-Length", "0").build();
        }

        // send OK with list of users
        return ResponseEntity.ok(users);
    }
}
