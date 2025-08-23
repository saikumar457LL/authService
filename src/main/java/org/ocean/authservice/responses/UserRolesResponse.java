package org.ocean.authservice.responses;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class UserRolesResponse {
    private String username;
    private List<String> roles;
}
