package org.ocean.authservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;

@Data
@Entity
@Table(name = "roles", schema = "ocean")
public class Roles implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String roleName;
    @Column(updatable = false)
    private Date createdDate;
    private Date modifiedDate;

    @Override
    public String getAuthority() {
        return roleName;
    }
}
