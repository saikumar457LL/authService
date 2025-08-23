package org.ocean.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.ocean.authservice.dao.UserSignUp;
import org.ocean.authservice.responses.ApiResponse;
import org.ocean.authservice.responses.UserRegisterSuccessResponse;
import org.ocean.authservice.serviceImpl.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
