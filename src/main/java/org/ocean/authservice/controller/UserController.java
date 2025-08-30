package org.ocean.authservice.controller;


import lombok.RequiredArgsConstructor;
import org.ocean.authservice.dao.UserProfileDao;
import org.ocean.authservice.entity.UserProfile;
import org.ocean.authservice.mappers.UserProfileMapper;
import org.ocean.authservice.responses.ApiResponse;
import org.ocean.authservice.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


    private final UserDetailsService userDetailsService;

    @PatchMapping
    public ResponseEntity<ApiResponse<UserProfileDao>> test(@RequestBody UserProfileDao userProfileDao) {
        UserProfileDao updateUserProfile = userDetailsService.updateUserProfile(userProfileDao);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<UserProfileDao>builder()
                                .success(true)
                                .status(HttpStatus.OK.value())
                                .timestamp(LocalDateTime.now())
                                .message("User profile updated successfully")
                                .data(updateUserProfile)
                                .build()
                );
    }
}
