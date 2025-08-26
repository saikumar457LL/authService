package org.ocean.authservice.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocean.authservice.dao.ModifyRoles;
import org.ocean.authservice.dao.RoleDao;
import org.ocean.authservice.entity.Roles;
import org.ocean.authservice.entity.User;
import org.ocean.authservice.entity.UserRoles;
import org.ocean.authservice.exceptions.RoleExists;
import org.ocean.authservice.exceptions.RoleNotFoundException;
import org.ocean.authservice.repository.RolesRepository;
import org.ocean.authservice.repository.UserRepository;
import org.ocean.authservice.repository.UserRolesRepository;
import org.ocean.authservice.responses.UserRolesResponse;
import org.ocean.authservice.services.RolesService;
import org.ocean.authservice.utils.UserUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
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
    public void addNewRole(RoleDao roles) {
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

        User user = userRepository.findByUsername(modifyRoles.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User: "+modifyRoles.getUsername()+" not found"));

        String actingUser = userUtils.getLoggedInUser().getUsername();
        Set<String> allowedRolesOnly = new HashSet<>(userUtils.getLoggedInUser().getRoles());

        Set<String> commonRoles = new HashSet<>(modifyRoles.getRemovedRoles());
        commonRoles.retainAll(modifyRoles.getAddedRoles());

        Set<String> rolesToRemove = new HashSet<>();
        Set<String> rolesToAdd = new HashSet<>();

        if (!modifyRoles.getRemovedRoles().isEmpty()) {
            rolesToRemove.addAll(modifyRoles.getRemovedRoles().stream()
                    .filter(allowedRolesOnly::contains).collect(Collectors.toSet()));
        }

        if (!modifyRoles.getAddedRoles().isEmpty()) {
            rolesToAdd.addAll(modifyRoles.getAddedRoles().stream()
                    .filter(allowedRolesOnly::contains).collect(Collectors.toSet()));
        }
        rolesToRemove.removeAll(commonRoles);
        rolesToAdd.removeAll(commonRoles);

        if(rolesToRemove.isEmpty() && rolesToAdd.isEmpty()){
            throw new RoleNotFoundException("No valid roles provided to add or remove", "Modify Roles");
        }
        Set<Roles> finalRolesToAdd = new HashSet<>(rolesRepository.findByRoleNameIn(rolesToAdd));

        List<UserRoles> currentUserRoles = userRolesRepository.findAllByUser(user);

        Set<String> existingRoleNames = currentUserRoles.stream()
                .map(userRole -> userRole.getRole().getRoleName())
                .collect(Collectors.toSet());

        boolean removingAll = rolesToRemove.containsAll(existingRoleNames);

        if (removingAll && finalRolesToAdd.isEmpty()) {
            throw new IllegalStateException("Cannot remove all roles from a user without assigning at least one role");
        }



        List<UserRoles> rolesMarkedForDeletion = currentUserRoles.stream()
                .filter(currentUser -> rolesToRemove.contains(currentUser.getRole().getRoleName())).toList();

        userRolesRepository.deleteAll(rolesMarkedForDeletion);

        // refresh after deletion
        Set<String> updatedRoleNamesAfterDeletion = userRolesRepository.findAllByUser(user).stream()
                .map(userRole -> userRole.getRole().getRoleName())
                .collect(Collectors.toSet());

        // use updated roles
        List<UserRoles> rolesMarkedForInsert = finalRolesToAdd.stream()
                .filter(role -> !updatedRoleNamesAfterDeletion.contains(role.getRoleName()))
                .map(role -> {
                    UserRoles userRoles = new UserRoles();
                    userRoles.setUser(user);
                    userRoles.setRole(role);
                    return userRoles;
                }).toList();
        userRolesRepository.saveAll(rolesMarkedForInsert);

        log.info("User '{}' modifying roles for '{}': ADD: {}, REMOVE: {}",
                actingUser, modifyRoles.getUsername(), rolesMarkedForInsert.stream().map(r -> r.getRole().getRoleName()).toList(), rolesMarkedForDeletion.stream().map(r -> r.getRole().getRoleName()).toList());

        List<String> updatedRoleNames = userRolesRepository.findAllByUser(user).stream()
                .map(userRole -> userRole.getRole().getRoleName())
                .toList();

        log.info("Final roles for user '{}': {}", user.getUsername(), updatedRoleNames);

        return UserRolesResponse.builder()
                .username(user.getUsername())
                .roles(updatedRoleNames)
                .build();
    }

    @Override
    public Set<RoleDao> fetchAllRoles() {
        return rolesRepository.findAll().stream().map(role -> RoleDao.builder().roleName(role.getRoleName()).build()).collect(Collectors.toSet());
    }
}
