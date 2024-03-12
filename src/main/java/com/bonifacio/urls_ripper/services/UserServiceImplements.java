package com.bonifacio.urls_ripper.services;

import com.bonifacio.urls_ripper.dtos.UrlsDetails;
import com.bonifacio.urls_ripper.dtos.UserDetails;
import com.bonifacio.urls_ripper.mappers.UrlMapper;
import com.bonifacio.urls_ripper.mappers.UserMapper;
import com.bonifacio.urls_ripper.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class UserServiceImplements implements UserService{
    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private JwtService _jwtService;
    @Autowired
    private final UrlMapper _urlMapper;
    @Autowired
    private final UserMapper _userMapper;
    /**
     * Finds a user by their username.
     * Retrieves the user from the UserRepository by their username.
     * If the user is found, converts the associated URLs to UrlsDetails using the UrlMapper.
     * Constructs a UserDetails object using the UserMapper with the user and associated URLs details.
     * If the user is not found, returns null.
     * @param username The username of the user to find.
     * @return The UserDetails object associated with the found user, or null if the user is not found.
     */
    @Override
    public UserDetails findUser(String username) {
        // Retrieve the user from the UserRepository by their username
        var userOptional = _userRepository.findByUsername(username);

        // Check if the user exists
        if (userOptional.isEmpty()) {
            return null; // Return null if the user is not found
        }

        // Get the user object
        var user = userOptional.get();

        // Convert the associated URLs to UrlsDetails using the UrlMapper
        ArrayList<UrlsDetails> urls = new ArrayList<>();
        user.getUrls().forEach(url ->
                urls.add(_urlMapper.userUrlToUserDetails(url))
        );

        // Constructs a UserDetails object using the UserMapper with the user and associated URLs details
        return _userMapper.userToUserDetails(user, urls);
    }


    /**
     * Finds a user by their authentication token.
     * Checks if the token is empty, and if so, returns null.
     * Extracts the username from the token using the JwtService.
     * Checks if the username is empty, and if so, returns null.
     * Retrieves the user from the UserRepository by the extracted username.
     * If the user is not found, throws an exception.
     * Converts the associated URLs to UrlsDetails using the UrlMapper.
     * Constructs a UserDetails object using the UserMapper with the user and associated URLs details.
     * @param token The authentication token to find the user.
     * @return The UserDetails object associated with the found user, or null if the token is empty or the user is not found.
     * @throws NoSuchElementException If the user is not found in the UserRepository.
     */
    @Override
    public UserDetails findUserByToken(String token) throws NoSuchElementException {
        // Check if the token is empty, and if so, return null
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        // Extract the username from the token using the JwtService
        var username = _jwtService.getUsernameFromToken(token);

        // Check if the username is empty, and if so, return null
        if (StringUtils.isEmpty(username)) {
            return null;
        }

        // Retrieve the user from the UserRepository by the extracted username
        var user = _userRepository.findByUsername(username).orElseThrow();

        // Convert the associated URLs to UrlsDetails using the UrlMapper
        ArrayList<UrlsDetails> urls = new ArrayList<>();
        user.getUrls().forEach(url ->
                urls.add(_urlMapper.userUrlToUserDetails(url))
        );

        // Constructs a UserDetails object using the UserMapper with the user and associated URLs details
        return _userMapper.userToUserDetails(user, urls);
    }


}
