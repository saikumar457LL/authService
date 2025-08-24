package org.ocean.authservice.controllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import org.ocean.authservice.exceptions.InvalidToken;
import org.ocean.authservice.responses.ApiResponse;
import org.ocean.authservice.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class TokenExceptionsHandler {

    @ExceptionHandler(InvalidToken.class)
    public ResponseEntity<ApiResponse<InvalidToken>> invalidJwtToken(Exception invalidToken, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(request.getRequestURI())
                .error(invalidToken.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.<InvalidToken>builder()
                        .success(false)
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message(invalidToken.getMessage())
                        .timestamp(LocalDateTime.now())
                        .error(errorResponse)
                        .build()
        );
    }
    
}
