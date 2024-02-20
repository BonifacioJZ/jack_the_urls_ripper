package com.bonifacio.urls_ripper.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user_urls")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class UserUrl extends Url{
    @NotNull
    @NotEmpty
    @Size(max = 150)
    @Column(columnDefinition = "varchar(250) default 'slug'")
    private String name;
    @Size(max = 500)
    @Column
    private String description;

}
