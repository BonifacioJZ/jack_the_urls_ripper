package com.bonifacio.urls_ripper.services;

import com.bonifacio.urls_ripper.dtos.UserDetails;

import java.util.UUID;

public interface UserService {
    UserDetails findUser(String username);

}
