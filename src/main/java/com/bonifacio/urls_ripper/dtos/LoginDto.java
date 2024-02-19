package com.bonifacio.urls_ripper.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LoginDto(
        @NotNull
        String username,
        @NotNull
        String password
) {
}
