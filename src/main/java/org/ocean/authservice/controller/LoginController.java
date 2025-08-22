package org.ocean.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.ocean.authservice.dao.UserLogin;
import org.ocean.authservice.exceptions.InvalidPassword;
import org.ocean.authservice.serviceImpl.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @PostMapping
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLogin userLogin) throws InvalidPassword {
        return ResponseEntity.ok(userDetailsServiceImpl.getToken(userLogin));
    }
}
