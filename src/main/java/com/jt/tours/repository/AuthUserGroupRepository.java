package com.jt.tours.repository;

import com.jt.tours.security.domain.AuthUserGroup;
import com.jt.tours.security.domain.AuthUserGroupEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Authority group repository interface
 *
 * Created by Jason Tao on 6/11/2020
 */
@Repository
public interface AuthUserGroupRepository extends JpaRepository<AuthUserGroup, Long> {

    AuthUserGroup findByAuthGroup(AuthUserGroupEnum authUserGroupEnum);
}
