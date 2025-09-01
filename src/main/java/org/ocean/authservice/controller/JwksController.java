package org.ocean.authservice.controller;


import lombok.RequiredArgsConstructor;
import org.ocean.authservice.jwt.KeyInfo;
import org.ocean.authservice.jwt.KeyManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPublicKey;
import java.util.*;

@RestController
@RequestMapping("/auth/.well-known")
@RequiredArgsConstructor
public class JwksController {
    private final KeyManager keyManager;

    @GetMapping("/jwks.json")
    public ResponseEntity<Map<String ,Object>> publicJwks() {
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

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        Map.of("keys", jwkList)
                );

    }
}
