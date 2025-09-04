package org.ocean.authservice.repository;

import jakarta.validation.constraints.NotBlank;
import org.ocean.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean  existsByUsername(String username);
    boolean  existsByEmail(String email);
    List<User> findAllByUuidIn(List<UUID> uuid);
}
