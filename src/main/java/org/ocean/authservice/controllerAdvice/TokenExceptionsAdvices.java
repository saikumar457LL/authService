package org.ocean.authservice.controllerAdvice;

import org.ocean.authservice.exceptions.InvalidToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class TokenExceptionsAdvices {

    @ExceptionHandler(InvalidToken.class)
    public ResponseEntity<Map<String, Object>> TokenExceptionsAdvice(InvalidToken invalidToken) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", invalidToken.getError()));
    }
}
