package com.jt.tours.security;

import com.jt.tours.repository.UserRepository;
import com.jt.tours.security.domain.CsrUserPrincipal;
import com.jt.tours.security.domain.User;
import com.jt.tours.security.jwt.JwtTokenProvider;
import com.jt.tours.web.rest.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

import static org.springframework.security.core.userdetails.User.withUsername;


/**
 * Service to associate user with password and roles in the database.
 *
 * Created by Jason Tao on 6/3/2020
 */
@Slf4j
@Component
public class CsrUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public CsrUserDetailsService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        super();
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Return the UserDetails for a specified username
     *
     * @param username the username of the user.
     * @return the UserDetails object consists of the user credential.
     * @throws UsernameNotFoundException exception when a user name do not exist.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Look up the user in database
        Optional<User> userOptional = this.userRepository.findByUsername(username);

        log.info("Looking up user {}", username);
        User foundUser = userOptional.orElseThrow(() -> new UsernameNotFoundException("User " + username + " do not exist."));

        return CsrUserPrincipal.builder()
                    .user(foundUser)
                    .authUserGroups(foundUser.getAuthUserGroups())
                    .build();
    }

    /**
     * Extract username and authority groups from a validated JWT token string
     *
     * @param token the JWT token string
     * @return UserDetail if valid, empty otherwise.
     */
    public Optional<UserDetails> loadUserByJwtToken(String token) throws CustomException {

        String                  extractUsername = jwtTokenProvider.getUserNameFromToken(token);
        Set<GrantedAuthority> extractAuthGroups = jwtTokenProvider.getAuthGroupsFromToken(token);

        log.info("Extracting user {} with auth group {}", extractUsername, extractAuthGroups);

        return Optional.of(withUsername(extractUsername)
                                    .authorities(extractAuthGroups)
                                    .password("")
                                    .accountExpired(false)
                                    .accountLocked(false)
                                    .credentialsExpired(false)
                                    .disabled(false)
                                    .build());
    }
}
