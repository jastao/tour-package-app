package com.jt.tours.web.rest.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Data transfer object that contains the user credential info.
 *
 * Created by Jason Tao on 6/9/2020
 */
@Data
public class UserDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;

    private String firstName;

    private String lastName;

    private String email;

    @Builder
    public UserDTO(String username, String password, String firstName, String lastName, String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
