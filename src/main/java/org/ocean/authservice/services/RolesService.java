package org.ocean.authservice.services;

import org.ocean.authservice.dao.ModifyRoles;
import org.ocean.authservice.dao.RoleDao;
import org.ocean.authservice.responses.UserRolesResponse;

import java.util.Set;

public interface RolesService {

    UserRolesResponse modifyUserRoles(ModifyRoles modifyRoles);
    void addNewRole(RoleDao roleDao);
    Set<RoleDao> fetchAllRoles();
}
