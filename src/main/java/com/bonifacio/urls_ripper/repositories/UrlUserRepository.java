package com.bonifacio.urls_ripper.repositories;

import com.bonifacio.urls_ripper.entities.UserUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UrlUserRepository extends JpaRepository<UserUrl, UUID> {
}
