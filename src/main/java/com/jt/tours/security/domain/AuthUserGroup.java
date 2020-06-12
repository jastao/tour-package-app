package com.jt.tours.security.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Role access group for a user.
 *
 * Created by Jason Tao on 6/3/2020
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "auth_user_group")
public class AuthUserGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_user_group_id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "auth_group")
    private AuthUserGroupEnum authGroup;

    @NotNull
    @Column(name = "description")
    private String description;

    @Builder
    public AuthUserGroup(AuthUserGroupEnum authGroup, String description) {
        this.authGroup = authGroup;
        this.description = description;
    }

}
