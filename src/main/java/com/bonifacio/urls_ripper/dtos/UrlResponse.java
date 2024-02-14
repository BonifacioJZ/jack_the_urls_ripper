package com.bonifacio.urls_ripper.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlResponse {
    private String url;
    private  String slug;
    private LocalDateTime expirationDate;

    @Override
    public String toString() {
        return "UrlResponse{" +
                "url='" + url + '\'' +
                ", slug='" + slug + '\'' +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
