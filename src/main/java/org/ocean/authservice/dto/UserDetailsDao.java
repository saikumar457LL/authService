package org.ocean.authservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDetailsDao {
    private String username;
    private String email;
    private List<String> roles;
    private boolean enabled;
    private UserProfileDao userProfileDao;
}
