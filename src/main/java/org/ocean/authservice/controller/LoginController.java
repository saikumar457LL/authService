package org.ocean.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.ocean.authservice.dto.TokenDto;
import org.ocean.authservice.dto.UserLogin;
import org.ocean.authservice.responses.ApiResponse;
import org.ocean.authservice.serviceImpl.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @PostMapping
    public ResponseEntity<ApiResponse<TokenDto>> login(@RequestBody UserLogin userLogin) {
        TokenDto token = userDetailsServiceImpl.getToken(userLogin);
        return ResponseEntity.ok(
                ApiResponse.<TokenDto>builder()
                        .status(HttpStatus.OK.value())
                        .message("Successfully logged in")
                        .data(token)
                        .success(true)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
