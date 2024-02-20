package com.bonifacio.urls_ripper.entities;

import jakarta.persistence.*;
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
    @JoinColumn(name="user_id")
    @ManyToOne(targetEntity = User.class)
    private User user;

}
