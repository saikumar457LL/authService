package org.ocean.authservice.jwt;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@Getter
public class KeyManager {
    ConcurrentHashMap<String , KeyInfo> keys = new ConcurrentHashMap<>();
    private volatile String currentKeyId;
    Duration keyExpireDuration = Duration.ofHours(1);

    public KeyManager() throws NoSuchAlgorithmException {
        rotateKey();
    }

    public void rotateKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        String KeyId = UUID.randomUUID().toString();
        keys.put(KeyId, KeyInfo.builder().keyPair(keyPair).createdAt(Instant.now()).retired(false).build());
        currentKeyId = KeyId;

    }
    public  KeyInfo getCurrentKey() {
        return keys.get(currentKeyId);
    }

    // Retire old keys after JWTs expire
    public void cleanupOldKeys() {
        Instant now = Instant.now();
        keys.entrySet().removeIf(entry ->
                entry.getValue().isRetired() &&
                        entry.getValue().getCreatedAt().plus(keyExpireDuration).isBefore(now)
        );
    }

    public void markCurrentKeyAsRetired() {
        keys.get(currentKeyId).setRetired(true);
    }
}
