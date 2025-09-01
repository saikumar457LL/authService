package org.ocean.authservice.controller;


import lombok.RequiredArgsConstructor;
import org.ocean.authservice.jwt.KeyInfo;
import org.ocean.authservice.jwt.KeyManager;
import org.ocean.authservice.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/auth/.well-known")
@RequiredArgsConstructor
public class JwksController {
    private final KeyManager keyManager;

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> publicJwks() {
        List<Map<String, Object>> jwkList = new ArrayList<>();

        for (Map.Entry<String, KeyInfo> entry : keyManager.getKeys().entrySet()) {
            String kid = entry.getKey();
            RSAPublicKey publicKey = (RSAPublicKey) entry.getValue().getPublic();

            Map<String, Object> jwk = new HashMap<>();
            jwk.put("kty", "RSA");
            jwk.put("alg", "RS256");
            jwk.put("use", "sig");
            jwk.put("kid", kid);
            jwk.put("n", Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getModulus().toByteArray()));
            jwk.put("e", Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getPublicExponent().toByteArray()));

            jwkList.add(jwk);
        }
        Map<String, List<Map<String, Object>>> jwks = Map.of("jwks", jwkList);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("Success")
                                .timestamp(LocalDateTime.now())
                                .data(jwks)
                                .build()
                );

    }
}
