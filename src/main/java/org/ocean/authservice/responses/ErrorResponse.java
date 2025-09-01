package org.ocean.authservice.responses;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorResponse {
    private String error;
    private String path;
    private List<FieldValidationError> fieldValidationErrors;
}
