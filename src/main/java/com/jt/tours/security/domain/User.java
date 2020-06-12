package com.jt.tours.security.domain;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Set;

/**
 * User object that contains the user information and credential.
 *
 * Created by Jason Tao on 6/3/2020
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @ApiModelProperty(notes = "The username of the user.")
    @Column(name = "username")
    private String username;

    @NotNull
    @ApiModelProperty(notes = "The password of the user.")
    @JsonIgnore
    @Column(name = "password")
    private String password;

    @NotNull
    @ApiModelProperty(notes = "The first name of the user.")
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @ApiModelProperty(notes = "The last name of the user.")
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Email
    @ApiModelProperty(notes = "User's email.")
    @Column(name = "email")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_auth_user_group",
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "auth_user_group_id", referencedColumnName = "auth_user_group_id")
    )
    private Set<AuthUserGroup> authUserGroups;

    @Builder
    public User(String username, String password, String firstName, String lastName, String email, Set<AuthUserGroup> authUserGroups) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.authUserGroups = authUserGroups;
    }
}
