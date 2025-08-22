package org.ocean.authservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.ocean.authservice.exceptions.InvalidToken;
import org.ocean.authservice.properties.JjwtProperties;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class JjwtUtils {
    private final JjwtProperties jwtProperties;

    private SecretKey getSecurityKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public HashMap<String, Object> generateToken(String username) {
        HashMap<String, Object> tokenDetails = new HashMap<>();
        Instant now = Instant.now();
        Date iAt = Date.from(now);
        Date iEt = Date.from(now.plusSeconds(Duration.ofHours(1).toSeconds()));
        String token = Jwts.builder()
                .signWith(getSecurityKey())
                .subject(username)
                .issuedAt(iAt)
                .expiration(iEt)
                .compact();
        tokenDetails.put("token", token);
        tokenDetails.put("token_type", "Bearer");
        tokenDetails.put("iAt", iAt);
        tokenDetails.put("iEt", iEt);
        return tokenDetails;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecurityKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean validateToken(Date expireAt) {
        return expireAt != null && expireAt.after(new Date());
    }

    public String getUserName(String token) throws InvalidToken {
        Claims claims = parseClaims(token);
        if (validateToken(claims.getExpiration())) {
            return claims.getSubject();
        }
        throw InvalidToken.builder().errorDescription("Token Expired").error("Invalid Token").build();
    }
}
