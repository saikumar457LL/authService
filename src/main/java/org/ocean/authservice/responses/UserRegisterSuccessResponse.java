package org.ocean.authservice.responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRegisterSuccessResponse {
    private String username;
    private String email;
    private String message;
    private List<String> roles;
}
