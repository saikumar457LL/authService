package org.ocean.authservice.controller;


import lombok.RequiredArgsConstructor;
import org.ocean.authservice.dto.UserProfileDao;
import org.ocean.authservice.responses.ApiResponse;
import org.ocean.authservice.services.UserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


    private final UserDetailsService userDetailsService;

    @PatchMapping
    public ResponseEntity<ApiResponse<UserProfileDao>> modifyUserProfile(@RequestBody UserProfileDao userProfileDao) {
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
