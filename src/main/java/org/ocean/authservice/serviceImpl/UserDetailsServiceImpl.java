package org.ocean.authservice.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ocean.authservice.constants.Roles;
import org.ocean.authservice.dao.*;
import org.ocean.authservice.entity.User;
import org.ocean.authservice.entity.UserProfile;
import org.ocean.authservice.exceptions.InvalidPassword;
import org.ocean.authservice.exceptions.RoleNotFoundException;
import org.ocean.authservice.exceptions.UserSignUpExceptions;
import org.ocean.authservice.jwt.JjwtUtils;
import org.ocean.authservice.mappers.UserProfileMapper;
import org.ocean.authservice.repository.RolesRepository;
import org.ocean.authservice.repository.UserProfileRepository;
import org.ocean.authservice.repository.UserRepository;
import org.ocean.authservice.responses.UserRegisterSuccessResponse;
import org.ocean.authservice.services.UserDetailsService;
import org.ocean.authservice.utils.UserUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JjwtUtils jjwtUtils;
    private final UserProfileMapper userProfileMapper;
    private final UserUtils userUtils;
    private final UserProfileRepository userProfileRepository;

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
        if (userRepository.existsByUsername(userSignUp.getUsername()) || userRepository.existsByEmail(userSignUp.getEmail())) {
            throw new UserSignUpExceptions("Username or email already taken", "Please try different username/email");
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

    @Override
    public List<UserDetailsDao> fetchAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDetailsDao.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .enabled(user.isEnabled())
                        .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                        .userProfileDao(
                                userProfileMapper.toDto(user.getUserProfile())
                        )
                        .build()).collect(Collectors.toList());
    }

    @Override
    public boolean userExists(String username) {
        boolean usernameStatus = userRepository.existsByUsername(username);
        if (usernameStatus) {
            throw new UserSignUpExceptions("Username exists", "Username already exists");
        }
        return false;
    }

    @Override
    public boolean emailExists(String email) {
        boolean emailStatus = userRepository.existsByEmail(email);
        if (emailStatus) {
            throw new UserSignUpExceptions("Email exists", "Email already exists");
        }
        return false;
    }

    @Override
    public UserProfileDao updateUserProfile(UserProfileDao userProfileDao) {
        User user;

        if (userProfileDao.getUsername() != null) {
            if(!userUtils.loggedInUsername().equals(userProfileDao.getUsername()) && !userUtils.getLoggedInRoles().contains(Roles.ROLE_ADMIN.toString())) {
                throw new IllegalArgumentException("You are not allowed to update another user's details.");
            }
            // username equals or admin is updating the user details
            user = userRepository.findByUsername(userProfileDao.getUsername()).orElseThrow(() -> new UsernameNotFoundException("USERNAME NOT FOUND"));
        }
        else
            user = userRepository.findByUsername(userUtils.loggedInUsername()).orElseThrow(() -> new UsernameNotFoundException("USERNAME NOT FOUND"));

        UserProfile existiUserProfile = user.getUserProfile();
        if (existiUserProfile == null) {
            existiUserProfile = userProfileMapper.toEntity(userProfileDao);
            user.setUserProfile(existiUserProfile);
        }

        // user update their permissive fields
        updateProfileField(existiUserProfile::setFirst_name, userProfileDao.getFirst_name());
        updateProfileField(existiUserProfile::setLast_name, userProfileDao.getLast_name());
        updateProfileBooleanField(existiUserProfile::setGender,userProfileDao.isGender());
        updateProfileDateField(existiUserProfile::setJoining_date, userProfileDao.getJoining_date());

        // only hr,admin,super persons can do
        if(userUtils.getLoggedInRoles().contains(Roles.ROLE_ADMIN.toString())){
            // job tile
            updateProfileField(existiUserProfile::setJob_title, userProfileDao.getJob_title());

            updateProfileDateField(existiUserProfile::setJoining_date,userProfileDao.getJoining_date());
            updateProfileDateField(existiUserProfile::setEnd_date,userProfileDao.getEnd_date());

            // line manager update
            if(null != userProfileDao.getLine_manager()) {
                String lineManager = userProfileDao.getLine_manager();

                // check if the existing and updating line-manager is same
                if(null != user.getUserProfile().getLineManager() && lineManager.equals(user.getUserProfile().getLineManager().getUsername()))
                    existiUserProfile.setLineManager(user.getUserProfile().getLineManager());
                else {
                    // set new line-manager
                    User newLineManager = userRepository.findByUsername(lineManager).orElseThrow(() -> new UsernameNotFoundException("Line manager not found"));
                    existiUserProfile.setLineManager(newLineManager);
                }
            } /*else {
                existiUserProfile.setLineManager(null);
            }*/

        }

        return userProfileMapper.toDto(userProfileRepository.save(existiUserProfile));
    }

    private void updateProfileField(Consumer<String> setter, String value) {
        if (value != null) {
            setter.accept(value);
        }
    }
    private void updateProfileBooleanField(Consumer<Boolean> setter, Boolean value) {
        if (value != null) {
            setter.accept(value);
        }
    }
    private void updateProfileDateField(Consumer<LocalDate> setter, LocalDate value) {
        if(value != null) {
            setter.accept(value);
        }
    }
}
