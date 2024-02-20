package com.bonifacio.urls_ripper.dtos;

import lombok.Builder;

@Builder
public record CustomResponse<T>(
        String message,
        boolean success,
        T data
)  {
}
