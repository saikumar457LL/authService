package org.ocean.authservice.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserNamesResponseDto {
    private String uuid;
    private String username;
    private String email;
}
