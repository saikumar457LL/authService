package org.ocean.authservice.controllerAdvice;

import org.ocean.authservice.exceptions.InvalidPassword;
import org.ocean.authservice.exceptions.UserSignUpExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class UserExceptionsAdvice {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","UserNotFound","message", e.getMessage()));
    }

    @ExceptionHandler(InvalidPassword.class)
    public ResponseEntity<Map<String,Object>> handleInvalidPassword(InvalidPassword e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error",e.getError(),"message", e.getMessage()));
    }

    @ExceptionHandler(UserSignUpExceptions.class)
    public ResponseEntity<Map<String,Object>> handleUserSignUpExceptions(UserSignUpExceptions e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error",e.getError(),"message", e.getMessage()));
    }
}
