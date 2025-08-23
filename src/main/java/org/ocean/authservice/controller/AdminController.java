package org.ocean.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.ocean.authservice.dao.ModifyRoles;
import org.ocean.authservice.responses.ApiResponse;
import org.ocean.authservice.responses.UserRolesResponse;
import org.ocean.authservice.services.RolesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ADMIN','SUPER')")
@RequiredArgsConstructor
public class AdminController {


    private final RolesService rolesService;

    @PostMapping("/modify_user_roles")
    public ResponseEntity<ApiResponse<UserRolesResponse>> modifyUserRoles(@RequestBody @Validated ModifyRoles modifyRoles) {

        UserRolesResponse userRolesResponse = rolesService.modifyUserRoles(modifyRoles);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<UserRolesResponse>builder()
                        .success(true)
                        .status(HttpStatus.OK.value())
                        .message("successfully modified the user roles")
                        .data(userRolesResponse)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

}
