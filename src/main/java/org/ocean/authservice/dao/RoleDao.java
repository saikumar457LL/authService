package org.ocean.authservice.dao;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDao {
    @NotNull
    @NotEmpty
    private String roleName;
}
