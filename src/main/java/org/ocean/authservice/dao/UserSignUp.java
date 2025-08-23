package org.ocean.authservice.dao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignUp {
    @NotNull
    @NotEmpty
    private String username;
    @Size(min = 8, max = 20)
    // TODO
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@!?#$&()_/<>\\d])[A-Za-z\\d@!?#$&()_/<>\\d]{8,}$",message = "Please Enter Valid Password")
    private String password;
    @NotNull
    @Email
    private String email;
}
