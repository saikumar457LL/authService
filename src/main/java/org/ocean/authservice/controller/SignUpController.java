package org.ocean.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.ocean.authservice.dao.UserSignUp;
import org.ocean.authservice.serviceImpl.UserDetailsServiceImpl;
import org.ocean.authservice.utils.UserRegisterSuccess;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<UserRegisterSuccess> signUp(@RequestBody UserSignUp userSignUp) {
        return ResponseEntity.ok(userDetailsServiceImpl.register(userSignUp));
    }

}
