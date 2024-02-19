package com.bonifacio.urls_ripper.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterDto(
        @NotNull
        @Size(max = 150)
        String username,
        @Email
        @NotNull
        String  email,
        @NotNull
        @Size(max = 32,min = 8)
        String password,
        String firstName,
        String lastName
) {
}
