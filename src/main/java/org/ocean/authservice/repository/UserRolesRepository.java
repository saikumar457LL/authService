package org.ocean.authservice.repository;

import org.ocean.authservice.entity.User;
import org.ocean.authservice.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {

    List<UserRoles> findAllByUser(User user);
}
