package org.ocean.authservice.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.ocean.authservice.dao.ModifyRoles;
import org.ocean.authservice.repository.UserRepository;
import org.ocean.authservice.repository.UserRolesRepository;
import org.ocean.authservice.responses.ModifyUserRolesResponse;
import org.ocean.authservice.services.RolesService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService {

    private final UserRolesRepository userRolesRepository;
    private final UserRepository userRepository;

    @Override
    public ModifyUserRolesResponse modifyUserRoles(ModifyRoles modifyRoles) {
        return null;
    }
}
