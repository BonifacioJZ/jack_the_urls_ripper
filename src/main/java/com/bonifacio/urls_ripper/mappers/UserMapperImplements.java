package com.bonifacio.urls_ripper.mappers;

import com.bonifacio.urls_ripper.dtos.RegisterDto;
import com.bonifacio.urls_ripper.dtos.UrlsDetails;
import com.bonifacio.urls_ripper.dtos.UserDetails;
import com.bonifacio.urls_ripper.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserMapperImplements implements UserMapper {
    /**
     * Converts a RegisterDto object to a User object.
     * @param registerDto The RegisterDto object to be converted to User.
     * @return A User object.
     */
    @Override
    public User registerDtoToUser(RegisterDto registerDto) {
        // Check if the RegisterDto object is null
        if(registerDto == null){
            return null; // Returns null if the RegisterDto object is null
        }
        // Builds and returns a User object using the Builder pattern
        return User.builder()
                .username(registerDto.username()) // Assigns the username from RegisterDto
                .email(registerDto.email()) // Assigns the email from RegisterDto
                .firstName(registerDto.firstName()) // Assigns the first name from RegisterDto
                .lastName(registerDto.lastName()) // Assigns the last name from RegisterDto
                .build(); // Builds and returns the User object
    }


    /**
     * Converts a User object to a UserDetails object.
     * Assigns a list of URLs details associated with the user.
     * @param user The User object to be converted to UserDetails.
     * @param urlsDetailsList The list of URLs details associated with the user.
     * @return A UserDetails object.
     */
    @Override
    public UserDetails userToUserDetails(User user, List<UrlsDetails> urlsDetailsList) {
        // Check if the User object is null
        if(user == null){
            return null; // Returns null if the User object is null
        }
        // Builds and returns a UserDetails object using the Builder pattern
        return UserDetails
                .builder()
                .id(user.getId()) // Assigns the user's id
                .firstName(user.getFirstName()) // Assigns the user's first name
                .lastName(user.getLastName()) // Assigns the user's last name
                .username(user.getUsername()) // Assigns the user's username
                .email(user.getEmail()) // Assigns the user's email
                .urls(urlsDetailsList) // Assigns the list of URLs details associated with the user
                .build(); // Builds and returns the UserDetails object
    }


    /**
     * Converts an Optional<User> object to a UserDetails object.
     * Assigns a list of URLs details associated with the user if present.
     * @param userOptional The Optional<User> object to be converted to UserDetails.
     * @param urlsDetailsList The list of URLs details associated with the user.
     * @return A UserDetails object if the user is present in the Optional, otherwise returns null.
     */
    @Override
    public UserDetails userToUserDetails(Optional<User> userOptional, List<UrlsDetails> urlsDetailsList) {
        return userOptional.map(user -> UserDetails
                        .builder()
                        .id(user.getId()) // Assigns the user's id
                        .firstName(user.getFirstName()) // Assigns the user's first name
                        .lastName(user.getLastName()) // Assigns the user's last name
                        .email(user.getEmail()) // Assigns the user's email
                        .username(user.getUsername()) // Assigns the user's username
                        .urls(urlsDetailsList) // Assigns the list of URLs details associated with the user
                        .build()) // Builds and returns the UserDetails object
                .orElse(null); // Returns null if the user is not present in the Optional
    }
}
