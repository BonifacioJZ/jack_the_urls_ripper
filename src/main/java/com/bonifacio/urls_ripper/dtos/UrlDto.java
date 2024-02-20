package com.bonifacio.urls_ripper.dtos;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;



@Builder
public record UrlDto(
        @NotNull
        @NotEmpty
        @Lob
        String url,
        String expirationDate//optional
) {

}
