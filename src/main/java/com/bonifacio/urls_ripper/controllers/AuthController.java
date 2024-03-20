package com.bonifacio.urls_ripper.controllers;

import com.bonifacio.urls_ripper.dtos.CustomResponse;
import com.bonifacio.urls_ripper.dtos.LoginDto;
import com.bonifacio.urls_ripper.dtos.RegisterDto;
import com.bonifacio.urls_ripper.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth/")
@AllArgsConstructor
@Tag(name = "Authorization Controller")
public class AuthController {
    @Autowired
    private final AuthService authService;
    @Operation(summary = "User Login",description = "Logs in a user with provided credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "The user was successfully logged in.\n" +
                    "  - Body: CustomResponse indicating a successful login.",
            content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400",description = """
                    Error occurred during validation or login process.
                      - Body: CustomResponse indicating the error.""")
    })
    @RequestMapping(value = "login/",method = RequestMethod.POST)
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity<>(CustomResponse.builder()
                    .message("Error to login")
                    .data(result.getFieldError())
                    .success(false).build(), HttpStatus.BAD_REQUEST);
        }
        var token = authService.login(loginDto);
        return new ResponseEntity<>(CustomResponse
                .builder()
                .success(true)
                .data(token)
                .message("logged")
                .build(),HttpStatus.OK);
    }

    @Operation(summary = "User Registration",description = "Registers a new user with provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = """
                    The user was successfully registered.
                      - Body: CustomResponse indicating a successful registration.""",
            content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400",description = """
                    Error occurred during validation or registration process.
                      - Body: CustomResponse indicating the error.""")
    })
    @RequestMapping(value = "register/",method = RequestMethod.POST)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto,BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity<>(CustomResponse
                    .builder()
                    .success(false)
                    .message("Error to register")
                    .data(result.getFieldError())
                    .build(),HttpStatus.BAD_REQUEST);
        }
        var token = authService.register(registerDto);
        return new ResponseEntity<>(CustomResponse
                .builder()
                .success(true)
                .message("register")
                .data(token)
                .build(),HttpStatus.OK);
    }
}
