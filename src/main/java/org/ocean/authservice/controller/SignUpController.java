package org.ocean.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.ocean.authservice.dao.UserSignUp;
import org.ocean.authservice.responses.UserRegisterSuccessResponse;
import org.ocean.authservice.serviceImpl.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @PostMapping
    ResponseEntity<UserRegisterSuccessResponse> signUp(@RequestBody @Validated UserSignUp userSignUp) {
        return ResponseEntity.ok(userDetailsServiceImpl.register(userSignUp));
    }

}
