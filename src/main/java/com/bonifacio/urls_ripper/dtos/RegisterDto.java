package com.bonifacio.urls_ripper.dtos;

import com.bonifacio.urls_ripper.validations.UniqueEmail;
import com.bonifacio.urls_ripper.validations.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterDto(
        @NotNull
        @Size(max = 150)
        @UniqueUsername
        String username,
        @Email
        @NotNull
        @UniqueEmail
        String  email,
        @NotNull
        @Size(max = 32,min = 8)
        String password,
        String firstName,
        String lastName
) {
}
