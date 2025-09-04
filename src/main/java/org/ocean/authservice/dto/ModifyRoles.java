package org.ocean.authservice.dto;

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
    @NotNull
    private List<String> removedRoles;
    @NotNull
    private List<String> addedRoles;
}
