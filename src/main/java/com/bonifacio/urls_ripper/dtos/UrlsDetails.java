package com.bonifacio.urls_ripper.dtos;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;
@Builder
public record UrlsDetails(
        UUID id,
        String name,
        String description,
        String link,
        LocalDateTime expirationDate,
        LocalDateTime creationDate
) {
}
