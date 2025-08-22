package org.ocean.authservice.utils;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRegisterSuccess {
    private String username;
    private String email;
    private String message;
    private List<String> roles;
}
