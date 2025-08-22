package org.ocean.authservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Data
@Service
@ConfigurationProperties(prefix = "user.signup")
public class UserSignUpProperties {
    private String passwordError;
}
