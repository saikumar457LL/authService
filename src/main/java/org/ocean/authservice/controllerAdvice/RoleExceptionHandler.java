package org.ocean.authservice.controllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.ocean.authservice.exceptions.RoleExists;
import org.ocean.authservice.responses.ApiResponse;
import org.ocean.authservice.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class RoleExceptionHandler {

    @ExceptionHandler(RoleExists.class)
    public ResponseEntity<ApiResponse<Void>> roleExists(RoleExists roleExists, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(roleExists.getError())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.<Void>builder()
                        .success(false)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(errorResponse)
                        .timestamp(LocalDateTime.now())
                        .message(roleExists.getMessage())
                        .build()
        );
    }
}
