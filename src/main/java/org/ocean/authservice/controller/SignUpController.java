package org.ocean.authservice.controller;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.ocean.authservice.dto.UserSignUp;
import org.ocean.authservice.responses.ApiResponse;
import org.ocean.authservice.responses.UserRegisterSuccessResponse;
import org.ocean.authservice.serviceImpl.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @PostMapping
    ResponseEntity<ApiResponse<UserRegisterSuccessResponse>> signUp(@RequestBody @Validated UserSignUp userSignUp) {

        UserRegisterSuccessResponse register = userDetailsServiceImpl.register(userSignUp);

        return ResponseEntity.status(HttpStatus.OK.value()).body(
                ApiResponse.<UserRegisterSuccessResponse>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("Successfully registered")
                        .data(register)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/{username}")
    ResponseEntity<ApiResponse<Boolean>> checkUserExists(@PathVariable String username) {
        boolean userExists = userDetailsServiceImpl.userExists(username);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
                ApiResponse.<Boolean>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("Username available to register")
                        .timestamp(LocalDateTime.now())
                        .data(userExists)
                        .build()
        );
    }

    @GetMapping("/check/{email}")
    ResponseEntity<ApiResponse<Boolean>> checkEmailExists(@PathVariable @Validated @Email String email) {
        boolean emailExists = userDetailsServiceImpl.emailExists(email);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
                ApiResponse.<Boolean>builder()
                        .status(HttpStatus.OK.value())
                        .success(true)
                        .message("Email available to register")
                        .timestamp(LocalDateTime.now())
                        .data(emailExists)
                        .build()
        );
    }

}
