package com.bonifacio.urls_ripper.dtos;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UrlUserDto(

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
}
