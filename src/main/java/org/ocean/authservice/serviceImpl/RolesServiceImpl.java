package org.ocean.authservice.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ocean.authservice.dao.ModifyRoles;
import org.ocean.authservice.repository.RolesRepository;
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
    private final RolesRepository rolesRepository;

    /**
     * the user can assign the roles he was having to the other users
     * example
     *  Admin has roles admin,user,manager
     *      he was able to assign the roles admin,user & manager only to the other user\
     *  user can't add other roles to the other user which he was not having
     * @param modifyRoles
     * @return
     */
    @Transactional
    @Override
    public ModifyUserRolesResponse modifyUserRoles(ModifyRoles modifyRoles) {


        return null;
    }
}
