package org.ocean.authservice.exceptions;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class InvalidPassword extends Exception {
    private String error;
    private String message;
    public InvalidPassword(String error, String message) {
        super(error + " : " + message);
        this.error = error;
        this.message = message;
    }
}
