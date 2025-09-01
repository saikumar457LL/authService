package org.ocean.authservice.jwt;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.PublicKey;
import java.time.Instant;

@Builder
@Data
@Slf4j
public class KeyInfo {
    private KeyPair keyPair;
    private Instant createdAt;
    private boolean retired;

    public PublicKey getPublic() {
        return keyPair.getPublic();
    }
}
