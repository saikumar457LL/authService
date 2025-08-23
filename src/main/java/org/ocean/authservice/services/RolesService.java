package org.ocean.authservice.services;

import org.ocean.authservice.dao.ModifyRoles;
import org.ocean.authservice.responses.UserRolesResponse;

public interface RolesService {

    UserRolesResponse modifyUserRoles(ModifyRoles modifyRoles);
}
