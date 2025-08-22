package org.ocean.authservice.responses;


import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ErrorResponse {
    private String message;
    private int status;
    private String error;
    private LocalDateTime timestamp;
    private String path;
    private List<FieldValidationError> fieldValidationErrors;
}
