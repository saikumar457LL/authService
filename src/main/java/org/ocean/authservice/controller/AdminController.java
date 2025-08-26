package org.ocean.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.ocean.authservice.dao.ModifyRoles;
import org.ocean.authservice.dao.RoleDao;
import org.ocean.authservice.dao.UserDetailsDao;
import org.ocean.authservice.responses.ApiResponse;
import org.ocean.authservice.responses.UserRolesResponse;
import org.ocean.authservice.services.RolesService;
import org.ocean.authservice.services.UserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ADMIN','SUPER')")
@RequiredArgsConstructor
public class AdminController {


    private final RolesService rolesService;
    private final UserDetailsService userDetailsService;

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
    public ResponseEntity<ApiResponse<String>> addNewRole(@RequestBody @Validated RoleDao roleDao) {
        rolesService.addNewRole(roleDao);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<String>builder()
                        .success(true)
                        .status(HttpStatus.OK.value())
                        .message("successfully added the role")
                        .data("Successfully added the role: " + roleDao.getRoleName())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/fetch_all_users")
    public ResponseEntity<ApiResponse<List<UserDetailsDao>>> fetchAllUsers() {
        List<UserDetailsDao> fetchedAllUsers = userDetailsService.fetchAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<UserDetailsDao>>builder()
                        .success(true)
                        .status(HttpStatus.OK.value())
                        .message("successfully fetched the users")
                        .data(fetchedAllUsers)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/fetch_all_roles")
    public ResponseEntity<ApiResponse<Set<RoleDao>>> fetchAllRoles() {
        Set<RoleDao> availableRoles = rolesService.fetchAllRoles();
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<Set<RoleDao>>builder()
                        .success(true)
                        .status(HttpStatus.OK.value())
                        .message("successfully fetched the roles")
                        .timestamp(LocalDateTime.now())
                        .data(availableRoles)
                        .build()
        );
    }

}
