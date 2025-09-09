package org.ocean.authservice.services;

import org.ocean.authservice.dto.AdminUserNamesRequestDto;
import org.ocean.authservice.responses.AdminUserNamesResponseDto;

import java.util.List;

public interface AdminService {

    List<AdminUserNamesResponseDto>  fetchUserDetailsFromUuids(AdminUserNamesRequestDto adminUserNamesRequestDto);
}
