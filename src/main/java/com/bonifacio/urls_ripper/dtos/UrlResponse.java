package com.bonifacio.urls_ripper.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
public record UrlResponse(
        String url,
        String slug,
        LocalDateTime expirationDate
) {
    @Override
    public String toString() {
        return "UrlResponse{" +
                "url='" + url + '\'' +
                ", slug='" + slug + '\'' +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
