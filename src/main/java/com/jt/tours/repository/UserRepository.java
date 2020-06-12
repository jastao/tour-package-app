package com.jt.tours.repository;

import com.jt.tours.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository Interface
 *
 * Created by Jason Tao on 6/3/2020
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find the user by username.
     *
     * @param  username
     * @return Optional of User, empty if the user exists.
     */
    Optional<User> findByUsername(String username);
}
