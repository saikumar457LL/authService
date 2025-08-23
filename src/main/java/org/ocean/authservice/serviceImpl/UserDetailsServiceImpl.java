package org.ocean.authservice.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ocean.authservice.constants.Roles;
import org.ocean.authservice.dao.TokenDto;
import org.ocean.authservice.dao.UserLogin;
import org.ocean.authservice.dao.UserSignUp;
import org.ocean.authservice.entity.User;
import org.ocean.authservice.exceptions.InvalidPassword;
import org.ocean.authservice.exceptions.RoleNotFoundException;
import org.ocean.authservice.exceptions.UserSignUpExceptions;
import org.ocean.authservice.jwt.JjwtUtils;
import org.ocean.authservice.repository.RolesRepository;
import org.ocean.authservice.repository.UserRepository;
import org.ocean.authservice.repository.UserRolesRepository;
import org.ocean.authservice.responses.UserRegisterSuccessResponse;
import org.ocean.authservice.services.UserDetailService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final UserRolesRepository userRolesRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JjwtUtils jjwtUtils;

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("USERNAME NOT FOUND"));
    }

    public TokenDto getToken(@Validated UserLogin userLogin) {
        User user = userRepository.findByUsername(userLogin.getUsername()).orElseThrow(() -> new UsernameNotFoundException("USERNAME NOT FOUND"));
        if (!bCryptPasswordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
            throw InvalidPassword.builder().error("Invalid credentials").message("Please Enter Valid credentials").build();
        }
        return jjwtUtils.generateToken(user.getUsername());
    }

    @Transactional
    @Override
    public UserRegisterSuccessResponse register(@Validated UserSignUp userSignUp) {
        if (userRepository.existsByUsername(userSignUp.getUsername())) {
            throw new UserSignUpExceptions("USERNAME ALREADY TAKEN", "Please try another username");
        }
        if (userRepository.existsByEmail(userSignUp.getEmail())) {
            throw new UserSignUpExceptions("EMAIL ALREADY TAKEN", "Please Try another email");
        }

        User user = new User();
        user.setUsername(userSignUp.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userSignUp.getPassword()));
        user.setEmail(userSignUp.getEmail());
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        org.ocean.authservice.entity.Roles defaultRole = rolesRepository.findByRoleName(Roles.ROLE_USER.toString()).orElseThrow(() -> new RoleNotFoundException("Role not found", "Requested role not found"));

        user.setRoles(List.of(defaultRole));

        User savedUser = userRepository.save(user);

        return UserRegisterSuccessResponse.builder()
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .roles(savedUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .message("user created successfully")
                .build();
    }
}
