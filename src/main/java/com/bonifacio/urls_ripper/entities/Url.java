package com.bonifacio.urls_ripper.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "urls")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    @NotEmpty
    @Size(max = 150)
    @Column(columnDefinition = "varchar(250) default 'generic'")
    private String name;
    @Size(max = 500)
    @Column
    private String description;
    @Lob
    @Column
    private String link;
    @Column()
    private String slug;
    @Column(name = "creation_data")
    private LocalDateTime creationData;
    @Column(name="expiration_data")
    private LocalDateTime expirationData;

    @Override
    public String toString() {
        return "Url{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", shortLink='" + slug + '\'' +
                ", creationData=" + creationData +
                ", expirationData=" + expirationData +
                '}';
    }
}
