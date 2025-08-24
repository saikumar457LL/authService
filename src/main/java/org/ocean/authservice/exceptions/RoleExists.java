package org.ocean.authservice.exceptions;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class RoleExists extends RuntimeException {
    private String message;
    private String error;
    public RoleExists(String message, String error) {
        super(message + ": " + error);
        this.message = message;
        this.error = error;
    }
}
