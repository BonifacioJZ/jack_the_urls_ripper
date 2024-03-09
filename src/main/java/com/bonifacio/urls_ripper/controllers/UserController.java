package com.bonifacio.urls_ripper.controllers;

import com.bonifacio.urls_ripper.dtos.CustomResponse;
import com.bonifacio.urls_ripper.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/user/")
@RestController
@AllArgsConstructor
@Tag(name = "User Controller")
public class UserController {
    private UserService _userService;
    @RequestMapping(value = "{username}/")
    @Transactional
    public ResponseEntity<?> getUser(@PathVariable("username") String username){
        var user = _userService.findUser(username);
        if (user == null) return new ResponseEntity<>(CustomResponse
                .builder()
                .success(false)
                .message("Not Found username")
                .data(null)
                .build(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(CustomResponse
                .builder()
                .success(true)
                .data(user)
                .message("User")
                .build(),HttpStatus.OK);

    }
    @RequestMapping(value = "profile/")
    @Transactional
    public ResponseEntity<?> profile(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearer){
        var user = _userService.findUserByToken(bearer.substring(7));
        if(user == null){
            return new ResponseEntity<>(CustomResponse
                    .builder()
                    .success(false)
                    .message("Error to get user for token")
                    .data(null),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(CustomResponse
                .builder()
                .success(true)
                .message("user information")
                .data(user)
                .build(),HttpStatus.OK);
    }
}
