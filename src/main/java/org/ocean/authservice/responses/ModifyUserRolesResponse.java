package org.ocean.authservice.responses;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ModifyUserRolesResponse {
    private String message;
    private HttpStatus status;
}
