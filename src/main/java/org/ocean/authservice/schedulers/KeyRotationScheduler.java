package org.ocean.authservice.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ocean.authservice.jwt.KeyManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeyRotationScheduler {
    private final KeyManager keyManager;

    @Scheduled(cron = "0 0 * * * *")
    public void rotateKeys() throws NoSuchAlgorithmException {
        keyManager.markCurrentKeyAsRetired();
        log.info("Rotating keys");
        keyManager.rotateKey();
        keyManager.cleanupOldKeys();
        log.info("Keys rotated at: {}", LocalDateTime.now());
        log.info("Keys rotated");
    }
}
