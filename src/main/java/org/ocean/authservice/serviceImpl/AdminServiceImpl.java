package org.ocean.authservice.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocean.authservice.dto.AdminUserNamesRequestDto;
import org.ocean.authservice.entity.User;
import org.ocean.authservice.mappers.AdminUsernamesResponseMapper;
import org.ocean.authservice.repository.UserRepository;
import org.ocean.authservice.responses.AdminUserNamesResponseDto;
import org.ocean.authservice.services.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final AdminUsernamesResponseMapper adminUsernamesResponseMapper;

    @Override
    public List<AdminUserNamesResponseDto> getAdminUserNames(AdminUserNamesRequestDto adminUserNamesRequestDto) {
        List<User> userNamesList = userRepository.findAllByUuidIn(adminUserNamesRequestDto.getUuids().stream().map(UUID::fromString).toList());
        return adminUsernamesResponseMapper.toAdminUsersNamesResponseDto(userNamesList);
    }
}
