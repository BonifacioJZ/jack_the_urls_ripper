package com.bonifacio.urls_ripper.services;

import com.bonifacio.urls_ripper.dtos.LoginDto;
import com.bonifacio.urls_ripper.dtos.RegisterDto;
import com.bonifacio.urls_ripper.entities.User;
import com.bonifacio.urls_ripper.mappers.UserMapper;
import com.bonifacio.urls_ripper.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class AuthServiceImplement implements AuthService{
    @Autowired
    private final UserRepository _userRepository;
    @Autowired
    private final JwtService _jwtService;
    @Autowired
    private final PasswordEncoder _passwordEncoder;
    @Autowired
    private final AuthenticationManager _authenticationManager;
    private final UserMapper _userMapper;


    /**
     * Performs user authentication and generates a JWT token for login.
     * Uses the provided login credentials (username and password) from the LoginDto
     * to authenticate the user using the AuthenticationManager.
     * Retrieves UserDetails based on the username from the UserRepository.
     * Generates a JWT token using the UserDetails obtained.
     * @param loginDto The LoginDto object containing username and password.
     * @return The JWT token generated for the authenticated user.
     * @throws AuthenticationException If authentication fails.
     * @throws NoSuchElementException If UserDetails for the given username is not found.
     */
    @Override
    public String login(LoginDto loginDto) throws AuthenticationException, NoSuchElementException {
        // Authenticate the user using the provided username and password
        _authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.username(), // Username from the LoginDto
                loginDto.password() // Password from the LoginDto
        ));

        // Retrieve UserDetails based on the username from the UserRepository
        UserDetails user = _userRepository.findByUsername(loginDto.username())
                .orElseThrow(); // Throws NoSuchElementException if UserDetails is not found

        // Generate a JWT token for the authenticated user using the UserDetails obtained
        return _jwtService.getToken(user);
    }

    /**
     * Registers a new user using the information provided in the RegisterDto.
     * Converts the RegisterDto object to a User object using the UserMapper.
     * Encodes the password from the RegisterDto using the PasswordEncoder.
     * Saves the user to the UserRepository.
     * Generates a JWT token for the registered user using the JwtService.
     * @param registerDto The RegisterDto object containing user registration information.
     * @return The JWT token generated for the registered user.
     */
    @Override
    public String register(RegisterDto registerDto) {
        // Convert the RegisterDto object to a User object using the UserMapper
        var user = _userMapper.registerDtoToUser(registerDto);

        // Encode the password from the RegisterDto using the PasswordEncoder
        user.setPassword(_passwordEncoder.encode(registerDto.password()));

        // Save the user to the UserRepository
        user = _userRepository.save(user);

        // Generate a JWT token for the registered user using the JwtService
        return _jwtService.getToken(user);
    }
}
