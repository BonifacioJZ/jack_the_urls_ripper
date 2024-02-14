package com.bonifacio.urls_ripper.dtos;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UrlDto {
    @Size(max = 150)
    private String name ;
    @Size(max = 500)
    private String description;
    @NotNull
    @NotEmpty
    @Lob
    private String url;
    private String expirationDate;// optional

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
