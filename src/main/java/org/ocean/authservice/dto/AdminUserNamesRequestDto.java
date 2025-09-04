package org.ocean.authservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminUserNamesRequestDto {
    @NotNull
    @NotEmpty
    private List<String> uuids;
}
