package org.ocean.authservice.services;

import org.ocean.authservice.dao.TokenDto;
import org.ocean.authservice.dao.UserDetailsDao;
import org.ocean.authservice.dao.UserLogin;
import org.ocean.authservice.dao.UserSignUp;
import org.ocean.authservice.exceptions.InvalidPassword;
import org.ocean.authservice.exceptions.RoleNotFoundException;
import org.ocean.authservice.responses.UserRegisterSuccessResponse;

import java.util.List;

public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
    TokenDto getToken(UserLogin userLogin) throws InvalidPassword;
    UserRegisterSuccessResponse register(UserSignUp userSignUp) throws RoleNotFoundException;
    List<UserDetailsDao> fetchAllUsers();
    boolean userExists(String username);
    boolean emailExists(String  email);
}
