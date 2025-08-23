package org.ocean.authservice.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private ErrorResponse error;
}
