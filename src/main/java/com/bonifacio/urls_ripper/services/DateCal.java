package com.bonifacio.urls_ripper.services;

import java.time.LocalDateTime;

public interface DateCal {
    LocalDateTime getExpirationData(String expirationDate, LocalDateTime creationData);
}
