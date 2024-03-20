package com.bonifacio.urls_ripper.controllers;

import com.bonifacio.urls_ripper.dtos.CustomResponse;
import com.bonifacio.urls_ripper.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/user/")
@RestController
@AllArgsConstructor
@Tag(name = "User Controller")
public class UserController {
    private UserService _userService;
    /**
     * Retrieves user details by username.
     * Maps to the GET request method for the specified username.
     * Retrieves the UserDetails object associated with the given username using the UserService.
     * If the user is not found, returns a ResponseEntity with a CustomResponse indicating "Not Found username" and HTTP status 404.
     * If the user is found, returns a ResponseEntity with a CustomResponse containing the user details, a success message, and HTTP status 200.
     * @param username The username of the user to retrieve.
     * @return A ResponseEntity containing the user details or a not found message.
     */
    @RequestMapping(value = "{username}/", method = RequestMethod.GET)
    @Transactional
    public ResponseEntity<?> getUser(@PathVariable("username") String username) {
        // Retrieve user details by username using the UserService
        var user = _userService.findUser(username);

        // Check if the user is not found
        if (user == null) {
            // Return a ResponseEntity with a CustomResponse indicating "Not Found username" and HTTP status 404
            return new ResponseEntity<>(CustomResponse.builder()
                    .success(false)
                    .message("Not Found username")
                    .data(null)
                    .build(), HttpStatus.NOT_FOUND);
        }

        // Return a ResponseEntity with a CustomResponse containing the user details, a success message, and HTTP status 200
        return new ResponseEntity<>(CustomResponse.builder()
                .success(true)
                .data(user)
                .message("User")
                .build(), HttpStatus.OK);
    }

    /**
     * Retrieves the profile information of the authenticated user.
     * Maps to the GET request method for the "profile" endpoint.
     * Retrieves the UserDetails object associated with the authentication token using the UserService.
     * If the user is not found, returns a ResponseEntity with a CustomResponse indicating an error to get the user for the token and HTTP status 404.
     * If the user is found, returns a ResponseEntity with a CustomResponse containing the user information, a success message, and HTTP status 200.
     * @param bearer The authorization bearer token containing the authentication token.
     * @return A ResponseEntity containing the user's profile information or an error message.
     */
    @RequestMapping(value = "profile/", method = RequestMethod.GET)
    @Transactional
    public ResponseEntity<?> profile(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearer) {
        // Retrieve the profile information of the authenticated user using the authentication token
        var user = _userService.findUserByToken(bearer.substring(7));

        // Check if the user is not found
        if (user == null) {
            // Return a ResponseEntity with a CustomResponse indicating an error to get the user for the token and HTTP status 404
            return new ResponseEntity<>(CustomResponse.builder()
                    .success(false)
                    .message("Error to get user for token")
                    .data(null).build(), HttpStatus.NOT_FOUND);
        }

        // Return a ResponseEntity with a CustomResponse containing the user information, a success message, and HTTP status 200
        return new ResponseEntity<>(CustomResponse.builder()
                .success(true)
                .message("user information")
                .data(user)
                .build(), HttpStatus.OK);
    }

}
