package com.bonifacio.urls_ripper.dtos;

import lombok.Builder;



@Builder
public record UrlErrorResponseDto(
        String status,
        String error
) {

    @Override
    public String toString() {
        return "UrlErrorResponseDto{" +
                "status='" + status + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
