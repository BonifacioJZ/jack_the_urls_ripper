package com.bonifacio.urls_ripper.repositories;

import com.bonifacio.urls_ripper.entities.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UrlRepository extends JpaRepository<Url, UUID> {
    public Url findBySlug(String slug);
}
