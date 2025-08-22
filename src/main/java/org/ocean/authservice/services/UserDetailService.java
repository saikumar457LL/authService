package org.ocean.authservice.services;

import org.ocean.authservice.dao.UserLogin;
import org.ocean.authservice.dao.UserSignUp;
import org.ocean.authservice.exceptions.InvalidPassword;
import org.ocean.authservice.exceptions.RoleNotFoundException;
import org.ocean.authservice.responses.UserRegisterSuccessResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface UserDetailService extends UserDetailsService {
    Map<String ,Object> getToken(UserLogin userLogin) throws InvalidPassword;
    UserRegisterSuccessResponse register(UserSignUp userSignUp) throws RoleNotFoundException;
}
