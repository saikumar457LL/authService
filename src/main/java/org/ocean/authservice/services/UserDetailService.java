package org.ocean.authservice.services;

import org.ocean.authservice.dao.UserLogin;
import org.ocean.authservice.dao.UserSignUp;
import org.ocean.authservice.exceptions.InvalidPassword;
import org.ocean.authservice.utils.UserRegisterSuccess;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface UserDetailService extends UserDetailsService {
    Map<String ,Object> getToken(UserLogin userLogin) throws InvalidPassword;
    UserRegisterSuccess register(UserSignUp userSignUp);
}
