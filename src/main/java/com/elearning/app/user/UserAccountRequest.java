package com.elearning.app.user;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserAccountRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<UserRole> roles;
}
