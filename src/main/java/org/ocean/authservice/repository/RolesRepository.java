package org.ocean.authservice.repository;

import org.ocean.authservice.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByRoleName(String role);

    List<Roles> findByRoleNameIn(Set<String> roles);

    boolean existsByRoleName(String roleName);
}
