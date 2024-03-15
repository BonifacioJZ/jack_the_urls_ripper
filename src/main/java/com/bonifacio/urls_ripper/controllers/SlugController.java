package com.bonifacio.urls_ripper.controllers;

import com.bonifacio.urls_ripper.dtos.*;
import com.bonifacio.urls_ripper.services.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@Tag(name = "Url Controller")
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
    @Operation(summary = "Creation simple Short Url",description = "Creates a slug for a URL and saves it in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = """
                    The slug was successfully created and saved.
                      - Body: UrlResponse containing the slug, URL, and expiration date.""",
            content = @Content(schema = @Schema(implementation = UrlResponse.class))),
            @ApiResponse(responseCode = "400",description = """
                    Error occurred during slug creation or validation of the request body.
                     - Body: UrlErrorResponseDto indicating the error.""",
            content = @Content(schema = @Schema(implementation = UrlErrorResponseDto.class)))
    })
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
    @RequestMapping(method = RequestMethod.GET, value = "get/{slug}/")
    @Transactional
    @Operation(
            summary = "Get Page by Slug",
            description = "Retrieves a URL by its slug and redirects the user to that URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = """
                    The user is redirected to the retrieved URL."""),
            @ApiResponse(responseCode = "400",description = """
                    The slug does not exist or the URL has expired.
                      - Body: UrlErrorResponseDto indicating the error.""",
            content = @Content(schema = @Schema(implementation = UrlErrorResponseDto.class)))
    })
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
    /**
     * Creates a user-specific URL.
     * Maps to the POST request method for the "/user_url/" endpoint.
     * Validates the URL data provided in the request body using UrlUserDto.
     * If validation fails, returns a ResponseEntity with a CustomResponse indicating an error to create the slug and HTTP status 400.
     * Generates a user-specific URL slug using the UrlService and the authentication token.
     * If the slug creation fails, returns a ResponseEntity with a UrlErrorResponseDto indicating an error in slug creation and HTTP status 400.
     * Persists the user-specific URL using the UrlService.
     * Returns a ResponseEntity with a UrlResponse containing the created URL details and HTTP status 201 upon successful creation.
     * @param token The authorization bearer token containing the authentication token.
     * @param urlUserDto The DTO containing the data to create the user-specific URL.
     * @param result The result of the URL data validation.
     * @return A ResponseEntity containing the created user-specific URL or an error message.
     */
    @RequestMapping(value = "/user_url/", method = RequestMethod.POST)
    @Transactional
    @Operation(summary = "Create User-Specific URL Slug",description = "Creates a user-specific URL slug and saves it in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = """
                    The slug was successfully created and saved.
                      - Body: UrlResponse containing the slug, URL, and expiration date.""",
            content = @Content(schema = @Schema(implementation = UrlResponse.class))),
            @ApiResponse(responseCode = "400",description = """
                    Error occurred during slug creation or validation of the request body.
                     - Body: CustomResponse indicating the error""",
            content = @Content(schema = @Schema(implementation = UrlErrorResponseDto.class)))
    })
    public ResponseEntity<?> urlUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearer,
                                     @Valid @RequestBody UrlUserDto urlUserDto, BindingResult result) {
        // Validate the URL data provided in the request body using UrlUserDto
        if (result.hasErrors()) {
            // Return a ResponseEntity with a CustomResponse indicating an error to create the slug and HTTP status 400
            return new ResponseEntity<>(CustomResponse.builder()
                    .message("Error to create slug")
                    .success(false)
                    .data(result.getFieldError()), HttpStatus.BAD_REQUEST);
        }

        // Generate a user-specific URL slug using the UrlService and the authentication token
        var url = _urlService.generateUserSlug(urlUserDto, bearer.substring(7));

        // Check if the slug creation fails
        if (url == null) {
            // Return a ResponseEntity with a UrlErrorResponseDto indicating an error in slug creation and HTTP status 400
            return new ResponseEntity<>(UrlErrorResponseDto.builder()
                    .error("Error to creation slug")
                    .status(String.valueOf(HttpStatus.BAD_REQUEST))
                    .build(), HttpStatus.BAD_REQUEST);
        }

        // Persist the user-specific URL using the UrlService
        _urlService.persitestUserUrl(url);

        // Return a ResponseEntity with a UrlResponse containing the created URL details and HTTP status 201 upon successful creation
        return new ResponseEntity<>(UrlResponse.builder()
                .url(url.getLink())
                .slug(url.getSlug())
                .expirationDate(url.getExpirationData())
                .build(), HttpStatus.CREATED);
    }

    /**
     * Retrieves the URL information by its slug.
     * Maps to the GET request method for the "/api/user/profile/urls/{slug}/" endpoint.
     * Retrieves the URL details associated with the provided slug using the UrlService.
     * If the URL is not found, returns a ResponseEntity with a CustomResponse indicating that the URL is not found and HTTP status 404.
     * If the URL is found, returns a ResponseEntity with a CustomResponse containing the URL information, a success message, and HTTP status 200.
     * @param id The slug of the URL to retrieve.
     * @return A ResponseEntity containing the URL information or a not found message.
     */
    @RequestMapping(value = "/api/user/profile/urls/{slug}/", method = RequestMethod.GET)
    @Transactional
    @Operation(summary = "Get URL by Slug",description = "Retrieves URL information by its slug.")
    @ApiResponses(value = {
            @ApiResponse(description = "Response schema",content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "200",description = """
                    URL information was successfully retrieved.
                      - Body: CustomResponse containing the URL information.""",
                    content = @Content(schema = @Schema(implementation = UrlsDetails.class))),
            @ApiResponse(responseCode = "404",description = "The URL with the provided slug does not exist.\n" +
                    "  - Body: CustomResponse indicating that the URL is not found.")
    })
    public ResponseEntity<?> getUrl(@PathVariable("slug") String id) {
        // Retrieve the URL information by its slug using the UrlService
        var url = _urlService.getUserUrlById(id);

        // Check if the URL is not found
        if (url == null) {
            // Return a ResponseEntity with a CustomResponse indicating that the URL is not found and HTTP status 404
            return new ResponseEntity<>(CustomResponse.builder()
                    .message("The url is not found")
                    .success(true)
                    .data(null), HttpStatus.NOT_FOUND);
        }

        // Return a ResponseEntity with a CustomResponse containing the URL information, a success message, and HTTP status 200
        return new ResponseEntity<>(CustomResponse.builder()
                .success(true)
                .message("url information")
                .data(url)
                .build(), HttpStatus.OK);
    }

    /**
     * Edits the details of a user URL by its slug.
     * Maps to the PUT request method for the "/api/user/profile/urls/{slug}/edit/" endpoint.
     * Updates the details of the user URL with the provided data using the UrlService.
     * If there are validation errors in the provided data, returns a ResponseEntity with a CustomResponse indicating an error to update and HTTP status 400.
     * If there is an error saving the updated URL details, returns a ResponseEntity with a CustomResponse indicating an error to save and HTTP status 400.
     * If the details of the user URL are successfully updated, returns a ResponseEntity with a CustomResponse indicating a successful update and HTTP status 200.
     * @param slug The slug of the user URL to edit.
     * @param userDto The data to update the user URL with.
     * @param result The result of the validation process for the provided data.
     * @return A ResponseEntity indicating the result of the edit operation.
     */
    @RequestMapping(value = "/api/user/profile/urls/{slug}/edit/", method = RequestMethod.PUT)
    @Operation(summary = "Update User URL",description = "Updates the details of a user-specific URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = """
                    The URL details were successfully updated.
                      - Body: CustomResponse indicating a successful update.""",
            content = @Content(schema = @Schema(implementation = UrlsDetails.class))),
            @ApiResponse(responseCode = "400",description = """
                    Error occurred during validation or updating of the URL details.
                      - Body: CustomResponse indicating the error.""")
    })
    @Transactional
    public ResponseEntity<?> editUrl(@PathVariable("slug") String slug,
                                     @Valid @RequestBody UrlUserDto userDto,
                                     BindingResult result) {
        // Check for validation errors in the provided data
        if (result.hasErrors()) {
            // Return a ResponseEntity with a CustomResponse indicating an error to update and HTTP status 400
            return new ResponseEntity<>(CustomResponse.builder()
                    .success(false)
                    .message("error to update")
                    .data(result.getFieldError()),
                    HttpStatus.BAD_REQUEST);
        }

        // Update the details of the user URL with the provided data using the UrlService
        var url = _urlService.updateUserUrl(slug, userDto);

        // Check if there was an error saving the updated URL details
        if (url == null) {
            // Return a ResponseEntity with a CustomResponse indicating an error to save and HTTP status 400
            return new ResponseEntity<>(CustomResponse.builder()
                    .message("Error to save")
                    .success(false)
                    .data(null), HttpStatus.BAD_REQUEST);
        }

        // Return a ResponseEntity with a CustomResponse indicating a successful update and HTTP status 200
        return new ResponseEntity<>(CustomResponse.builder()
                .success(true)
                .message("Updated")
                .data(url), HttpStatus.OK);
    }

    /**
     * Deletes a URL by its slug.
     * Maps to the DELETE request method for the "/api/user/profile/urls/{slug}/delete/" endpoint.
     * Retrieves the encoded URL object associated with the provided slug using the UrlService.
     * If there is an error encoding the slug, returns a ResponseEntity with a CustomResponse indicating an error to encode and HTTP status 400.
     * Deletes the URL using the UrlService.
     * If the URL is successfully deleted, returns a ResponseEntity with a CustomResponse indicating a successful deletion and HTTP status 200.
     * If there is an error saving the deletion, returns a ResponseEntity with a CustomResponse indicating an error to save and HTTP status 400.
     * @param slug The slug of the URL to delete.
     * @return A ResponseEntity indicating the result of the delete operation.
     */

    @Operation(summary = "Delete URL by Slug",description = "Deletes a URL by its slug.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = """
                    The URL was successfully deleted.
                      - Body: CustomResponse indicating a successful deletion.""",
            content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "404",description = """
                    Error occurred during deletion or encoding the slug.
                      - Body: CustomResponse indicating the error.""",
            content = @Content(schema = @Schema(implementation = CustomResponse.class)))
    })
    @RequestMapping(value = "api/user/profile/urls/{slug}/delete/", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUrl(@PathVariable("slug") String slug) {
        try {
            // Retrieve the encoded URL object associated with the provided slug using the UrlService
            var url = _urlService.getEncodeUrl(slug);

            // Check if there is an error encoding the slug
            if (url == null) {
                // Return a ResponseEntity with a CustomResponse indicating an error to encode and HTTP status 400
                return new ResponseEntity<>(CustomResponse.builder()
                        .message("Error to encode")
                        .data(null)
                        .success(false), HttpStatus.BAD_REQUEST);
            }

            // Delete the URL using the UrlService
            _urlService.deleteSlug(url);

            // Return a ResponseEntity with a CustomResponse indicating a successful deletion and HTTP status 200
            return new ResponseEntity<>(CustomResponse.builder()
                    .success(true)
                    .message("Deleted")
                    .data(null), HttpStatus.OK);
        } catch (Exception e) {
            // Return a ResponseEntity with a CustomResponse indicating an error to save and HTTP status 400
            return new ResponseEntity<>(CustomResponse.builder()
                    .success(false)
                    .message("Error to save")
                    .data(e), HttpStatus.BAD_REQUEST);
        }
    }


}
