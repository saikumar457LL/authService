package org.ocean.authservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLogin {
    @NotNull
    private String username;
    @NotNull
    @Size(min=8, max=20)
    private String password;
    private String email;
}
