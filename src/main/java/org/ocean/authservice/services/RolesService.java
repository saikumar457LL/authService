package org.ocean.authservice.services;

import org.ocean.authservice.dao.ModifyRoles;
import org.ocean.authservice.responses.ModifyUserRolesResponse;

public interface RolesService {

    ModifyUserRolesResponse modifyUserRoles(ModifyRoles modifyRoles);
}
