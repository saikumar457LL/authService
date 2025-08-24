package org.ocean.authservice.controllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import org.ocean.authservice.exceptions.InvalidPassword;
import org.ocean.authservice.exceptions.UserSignUpExceptions;
import org.ocean.authservice.responses.ApiResponse;
import org.ocean.authservice.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class UserExceptionsHandler {

    @ExceptionHandler({UsernameNotFoundException.class,InvalidPassword.class})
    public ResponseEntity<ApiResponse<UsernameNotFoundException>> handleUsernameNotFoundException(UsernameNotFoundException e, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("Invalid credentials")
                .path(request.getRequestURI())
                .error(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.<UsernameNotFoundException>builder()
                        .success(false)
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .error(errorResponse)
                        .build()
        );
    }

    @ExceptionHandler(UserSignUpExceptions.class)
    public ResponseEntity<ApiResponse<UserSignUpExceptions>> handleUserSignUpExceptions(UserSignUpExceptions e, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.<UserSignUpExceptions>builder()
                        .success(false)
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .error(errorResponse)
                        .build()
        );
    }
}
