package com.bonifacio.urls_ripper.dtos;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;



@Builder
public record UrlDto(
        @Size(max = 150)
        String name,
        @Size(max = 500)
        String description,
        @NotNull
        @NotEmpty
        @Lob
        String url,
        String expirationDate//optional
) {

    @Override
    public String toString() {
        return "UrlDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                '}';
    }
}
