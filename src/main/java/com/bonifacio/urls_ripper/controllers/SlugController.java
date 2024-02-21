package com.bonifacio.urls_ripper.controllers;

import com.bonifacio.urls_ripper.dtos.*;
import com.bonifacio.urls_ripper.services.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@AllArgsConstructor
public class SlugController {
    private final UrlService _urlService;

    /**
     * This function handles the creation of a slug for a given URL and returns the
     * slug along with
     * other details in the response.
     * 
     * @param urlDto An object of type UrlDto, which contains the necessary
     *               information for creating a
     *               slug. It may include fields such as name, link, and
     *               expirationData.
     * @param result The `result` parameter is an instance of the `BindingResult`
     *               class, which is used
     *               to store and retrieve validation errors that occur during the
     *               validation process. It is used to
     *               check if there are any validation errors in the `urlDto`
     *               object.
     * @return The method is returning a ResponseEntity object.
     */
    @RequestMapping(method = RequestMethod.POST, value = "")
    public ResponseEntity<?> createSlug(@Valid @RequestBody UrlDto urlDto, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(UrlErrorResponseDto
                    .builder()
                    .error(Objects.requireNonNull(result.getFieldError()).toString())
                    .status(String.valueOf(HttpStatus.BAD_REQUEST))
                    .build(), HttpStatus.BAD_REQUEST);
        }
        var urlRes = _urlService.generateSlug(urlDto);
        if (urlRes == null) {
            return new ResponseEntity<>(UrlErrorResponseDto
                    .builder()
                    .error("Error to create slug")
                    .status(String.valueOf(HttpStatus.BAD_REQUEST))
                    .build(), HttpStatus.BAD_REQUEST);
        }
        _urlService.persitenstUrl(urlRes);
        return new ResponseEntity<>(UrlResponse
                .builder()
                .slug(urlRes.getSlug())
                .url(urlRes.getLink())
                .expirationDate(urlRes.getExpirationData())
                .build(), HttpStatus.CREATED);
    }
    /**
     * The function handles a GET request to retrieve a page based on a given slug,
     * and redirects the
     * user to the corresponding URL if it exists and is not expired.
     *
     * @param slug     The "slug" parameter is a string that represents a unique
     *                 identifier for a specific
     *                 page or resource. It is used to retrieve the corresponding
     *                 URL from the database.
     * @param response The "response" parameter is an instance of the
     *                 HttpServletResponse class, which
     *                 is used to send a response back to the client. In this case,
     *                 it is used to redirect the client
     *                 to the URL stored in the "url" object. The sendRedirect()
     *                 method is called on the response
     *                 object to perform
     * @return In this code snippet, the method is returning a `ResponseEntity`
     *         object. The specific
     *         type of the response entity depends on the conditions in the code.
     */
    @RequestMapping(method = RequestMethod.GET, value = "{slug}")
    @Transactional
    public ResponseEntity<?> getPage(@PathVariable String slug, HttpServletResponse response) throws IOException {
        var url = _urlService.getEncodeUrl(slug);
        if (url == null) {
            return new ResponseEntity<>(UrlErrorResponseDto
                    .builder()
                    .status(String.valueOf(HttpStatus.BAD_REQUEST))
                    .error("The slug is not exit")
                    .build(), HttpStatus.BAD_REQUEST);
        }
        if (url.getExpirationData().isBefore(LocalDateTime.now())) {
            _urlService.deleteSlug(url);
            return new ResponseEntity<>(UrlErrorResponseDto
                    .builder()
                    .error("The Url Expired, Please try generate a fresh one")
                    .status(String.valueOf(HttpStatus.BAD_REQUEST))
                    .build(), HttpStatus.BAD_REQUEST);

        }
        response.sendRedirect(url.getLink());
        return null;
    }
    @RequestMapping(value = "/userUrl/",method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<?> urlUser(@RequestHeader(value = "Bearer") String token,
                                     @Valid @RequestBody UrlUserDto urlUserDto,BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity<>(CustomResponse
                    .builder()
                    .message("Error to create slug")
                    .success(false)
                    .data(result.getFieldError()),HttpStatus.BAD_REQUEST);
        }
        var url = _urlService.generateUserSlug(urlUserDto,token);
        if(url == null){
            return new ResponseEntity<>(UrlErrorResponseDto
                    .builder()
                    .error("Error to creation slug")
                    .status(String.valueOf(HttpStatus.BAD_REQUEST))
                    .build(),HttpStatus.BAD_REQUEST);

        }
        _urlService.persitestUserUrl(url);
        return new ResponseEntity<>(UrlResponse
                .builder()
                .url(url.getLink())
                .slug(url.getSlug())
                .expirationDate(url.getExpirationData())
                .build(),HttpStatus.CREATED);
    }

}
