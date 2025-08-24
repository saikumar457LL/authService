package org.ocean.authservice.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocean.authservice.dao.ModifyRoles;
import org.ocean.authservice.dao.RolesAddDao;
import org.ocean.authservice.entity.Roles;
import org.ocean.authservice.entity.User;
import org.ocean.authservice.entity.UserRoles;
import org.ocean.authservice.exceptions.RoleExists;
import org.ocean.authservice.repository.RolesRepository;
import org.ocean.authservice.repository.UserRepository;
import org.ocean.authservice.repository.UserRolesRepository;
import org.ocean.authservice.responses.UserRolesResponse;
import org.ocean.authservice.services.RolesService;
import org.ocean.authservice.utils.UserUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService {

    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;
    private final RolesRepository rolesRepository;
    private final UserUtils userUtils;


    @Transactional
    @Override
    public void addNewRole(RolesAddDao roles) {
        if (rolesRepository.existsByRoleName(roles.getRoleName())) {
            throw new RoleExists("The given Role: "+roles.getRoleName() +" already exists","Role exists");
        }
        Roles role = new Roles();
        role.setRoleName(roles.getRoleName());
        role.setCreatedDate(Date.from(Instant.now()));
        role.setModifiedDate(Date.from(Instant.now()));
        rolesRepository.save(role);
    }


    /**
     * example
     *  Admin has roles admin,user,manager
     *      he was able to assign the roles admin,user & manager only to the other user\
     *  user can't add other roles to the other user which he was not having
     * @param modifyRoles
     * @return
     */
    @Transactional
    @Override
    public UserRolesResponse modifyUserRoles(ModifyRoles modifyRoles) {
        String actingUser = userUtils.getLoggedInUser().getUsername();
        List<String> allowedRolesOnly = userUtils.getLoggedInUser().getRoles();

        LinkedList<String> rolesToRemove = new LinkedList<>();
        LinkedList<String> rolesToAdd = new LinkedList<>();

        if (modifyRoles.getRemovedRoles() != null) {
            rolesToRemove.addAll(modifyRoles.getRemovedRoles().stream()
                    .filter(allowedRolesOnly::contains).toList());
        }

        if (modifyRoles.getAddedRoles() != null) {
            rolesToAdd.addAll(modifyRoles.getAddedRoles().stream()
                    .filter(allowedRolesOnly::contains).toList());
        }

        log.info("User '{}' modifying roles for '{}': ADD: {}, REMOVE: {}",
                actingUser, modifyRoles.getUsername(), rolesToAdd, rolesToRemove);

        User user = userRepository.findByUsername(modifyRoles.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(modifyRoles.getUsername()));

        List<Roles> finalRolesToRemove = rolesRepository.findByRoleNameIn(rolesToRemove);
        List<Roles> finalRolesToAdd = rolesRepository.findByRoleNameIn(rolesToAdd);

        List<UserRoles> currentUserRoles = userRolesRepository.findAllByUser(user);

        Set<String> existingRoleNames = currentUserRoles.stream()
                .map(userRole -> userRole.getRole().getRoleName())
                .collect(Collectors.toSet());

        if (existingRoleNames.size() == rolesToRemove.size() && finalRolesToAdd.isEmpty()) {
            throw new IllegalStateException("Cannot remove all roles from a user without assigning at least one role");
        }

        finalRolesToRemove.forEach(role -> {
            currentUserRoles.stream()
                    .filter(currentUser -> currentUser.getRole().getRoleName().equals(role.getRoleName()))
                    .findFirst()
                    .ifPresent(userRolesRepository::delete);
        });

        finalRolesToAdd.forEach(role -> {
            if (!existingRoleNames.contains(role.getRoleName())) {
                UserRoles userRoles = new UserRoles();
                userRoles.setUser(user);
                userRoles.setRole(role);
                userRolesRepository.save(userRoles);
            }
        });

        List<String> updatedRoleNames = userRolesRepository.findAllByUser(user).stream()
                .map(userRole -> userRole.getRole().getRoleName())
                .toList();

        return UserRolesResponse.builder()
                .username(user.getUsername())
                .roles(updatedRoleNames)
                .build();
    }
}
