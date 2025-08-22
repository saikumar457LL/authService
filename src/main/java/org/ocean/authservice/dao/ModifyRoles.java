package org.ocean.authservice.dao;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ModifyRoles {
    @NotNull
    @NotEmpty
    private String username;
    private List<String> removedRoles;
    private List<String> addedRoles;
}
