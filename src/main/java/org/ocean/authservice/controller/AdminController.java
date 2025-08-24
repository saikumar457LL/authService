package org.ocean.authservice.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ocean.authservice.dao.ModifyRoles;
import org.ocean.authservice.dao.RolesAddDao;
import org.ocean.authservice.responses.ApiResponse;
import org.ocean.authservice.responses.UserRolesResponse;
import org.ocean.authservice.services.RolesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ADMIN','SUPER')")
@RequiredArgsConstructor
public class AdminController {


    private final RolesService rolesService;

    @PatchMapping("/modify_user_roles")
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

    @PostMapping("/add_role")
    public ResponseEntity<ApiResponse<String>> addNewRole(@RequestBody @Validated RolesAddDao rolesAddDao) {
        rolesService.addNewRole(rolesAddDao);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<String>builder()
                        .success(true)
                        .status(HttpStatus.OK.value())
                        .message("successfully added the role")
                        .data("Successfully added the role: "+rolesAddDao.getRoleName())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

}
