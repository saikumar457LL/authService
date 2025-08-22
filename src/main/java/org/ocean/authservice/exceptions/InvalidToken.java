package org.ocean.authservice.exceptions;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class InvalidToken extends Exception {
    private String errorDescription;
    private String error;
    public InvalidToken(String error, String message) {
        super(error + " : " + message);
        this.error = error;
        this.errorDescription = message;
    }
}
