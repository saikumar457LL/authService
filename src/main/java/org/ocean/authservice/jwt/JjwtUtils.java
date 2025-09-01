package org.ocean.authservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.ocean.authservice.dao.TokenDto;
import org.ocean.authservice.exceptions.InvalidToken;
import org.ocean.authservice.properties.JjwtProperties;
import org.ocean.authservice.utils.UserUtils;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JjwtUtils {
    private final JjwtProperties jwtProperties;

    private final KeyManager keyManager;
    private final UserUtils userUtils;

    private SecretKey getSecurityKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public TokenDto generateToken(String username) {
        Instant now = Instant.now();
        Date iAt = Date.from(now);
        Date iEt = Date.from(now.plusSeconds(Duration.ofHours(1).toSeconds()));
        KeyPair currentKey = keyManager.getCurrentKey().getKeyPair();
        String currentKeyId = keyManager.getCurrentKeyId();

        String token = Jwts.builder()
                .signWith(currentKey.getPrivate(),Jwts.SIG.RS256)
                .subject(username)
                .issuedAt(iAt)
                .expiration(iEt)
                .header().add("kid",currentKeyId).and()
                .compact();

        return TokenDto.builder()
                .token(token)
                .iAt(iAt)
                .eAt(iEt)
                .tokenType("Bearer")
                .build();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(keyManager.getCurrentKey().getKeyPair().getPublic())
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
        throw InvalidToken.builder().error("TOKEN EXPIRED").message("Invalid token, please generate new token").build();
    }
}
