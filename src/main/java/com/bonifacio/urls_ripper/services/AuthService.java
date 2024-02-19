package com.bonifacio.urls_ripper.services;

import com.bonifacio.urls_ripper.dtos.LoginDto;
import com.bonifacio.urls_ripper.dtos.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}
