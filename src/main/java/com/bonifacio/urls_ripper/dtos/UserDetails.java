package com.bonifacio.urls_ripper.dtos;

import lombok.Builder;

import java.util.List;
import java.util.UUID;
@Builder
public record UserDetails(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        List<UrlsDetails> urls
) {
}
