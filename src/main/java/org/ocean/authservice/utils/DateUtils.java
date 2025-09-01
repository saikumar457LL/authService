package org.ocean.authservice.utils;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class DateUtils {

    public static Date getCurrentDate() {
        return Date.from(Instant.now());
    }
}
