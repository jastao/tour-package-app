package com.jt.tours.security.domain;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User Principal object used for authentication.
 *
 * Created by Jason Tao on 6/3/2020
 */
public class CsrUserPrincipal implements UserDetails {

    private User user;

    private Set<AuthUserGroup> authUserGroups;

    @Builder
    public CsrUserPrincipal(User user, Set<AuthUserGroup> authUserGroups) {
        this.user = user;
        this.authUserGroups = authUserGroups;
    }

    /**
     * The authorization user group privilege for the user.
     *
     * @return set of granted authority for the user.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // Return an empty set if the list of auth user group is null.
        if(null == this.authUserGroups) {
            return Collections.emptySet();
        }
        return this.authUserGroups.stream()
                .map(userGroup -> {
                    SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userGroup.getAuthGroup().getLabel());
                    return grantedAuthority;
                }).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
