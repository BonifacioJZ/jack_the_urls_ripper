package com.bonifacio.urls_ripper.mappers;

import com.bonifacio.urls_ripper.dtos.UrlDto;
import com.bonifacio.urls_ripper.dtos.UrlUserDto;
import com.bonifacio.urls_ripper.dtos.UrlsDetails;
import com.bonifacio.urls_ripper.entities.Url;
import com.bonifacio.urls_ripper.entities.UserUrl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UrlMapperImplements implements UrlMapper{
    /**
     * Converts a UrlDto object to a Url object.
     * If the UrlDto object is null, returns null.
     * @param urlDto The UrlDto object to be converted to Url.
     * @return A Url object with the current creation date and the URL from the UrlDto, or null if the UrlDto is null.
     */
    @Override
    public Url urlDtoToUrl(UrlDto urlDto) {
        // Check if the UrlDto object is null
        if(urlDto == null){
            return null; // Returns null if the UrlDto object is null
        }
        // Builds and returns a Url object using the Builder pattern
        return Url.builder()
                .creationData(LocalDateTime.now()) // Assigns the current creation date
                .link(urlDto.url()) // Assigns the URL from the UrlDto
                .build(); // Builds and returns the Url object
    }

    /**
     * Converts a UrlUserDto object to a UserUrl object.
     * If the UrlUserDto object is null, returns null.
     * @param urlUserDto The UrlUserDto object to be converted to UserUrl.
     * @return A UserUrl object with the name, link, description, and current creation date from the UrlUserDto,
     * or null if the UrlUserDto is null.
     */
    @Override
    public UserUrl urlUserDtoToUserUrl(UrlUserDto urlUserDto) {
        // Check if the UrlUserDto object is null
        if(urlUserDto == null){
            return null; // Returns null if the UrlUserDto object is null
        }
        // Builds and returns a UserUrl object using the Builder pattern
        return UserUrl.builder()
                .name(urlUserDto.name()) // Assigns the name from the UrlUserDto
                .link(urlUserDto.url()) // Assigns the link from the UrlUserDto
                .description(urlUserDto.description()) // Assigns the description from the UrlUserDto
                .creationData(LocalDateTime.now()) // Assigns the current creation date
                .build(); // Builds and returns the UserUrl object
    }
    /**
     * Converts an Optional<UserUrl> object to a UrlsDetails object.
     * If the Optional is empty, returns null.
     * @param url The Optional<UserUrl> object to be converted to UrlsDetails.
     * @return A UrlsDetails object with details extracted from the UserUrl if present in the Optional, otherwise returns null.
     */
    @Override
    public UrlsDetails userUrlToUserDetails(Optional<UserUrl> url) {
        // Check if the Optional is empty
        if(url.isEmpty()){
            return null; // Returns null if the Optional is empty
        }
        // Maps the UserUrl inside the Optional to a UrlsDetails object using the Builder pattern
        return url.map(userUrl -> UrlsDetails
                        .builder()
                        .id(userUrl.getId()) // Assigns the id from the UserUrl
                        .expirationDate(userUrl.getExpirationData()) // Assigns the expiration date from the UserUrl
                        .creationDate(userUrl.getCreationData()) // Assigns the creation date from the UserUrl
                        .name(userUrl.getName()) // Assigns the name from the UserUrl
                        .description(userUrl.getDescription()) // Assigns the description from the UserUrl
                        .slug(userUrl.getSlug()) // Assigns the slug from the UserUrl
                        .link(userUrl.getLink()) // Assigns the link from the UserUrl
                        .build()) // Builds and returns the UrlsDetails object
                .orElse(null); // Returns null if the UserUrl is not present in the Optional
    }

    /**
     * Converts a UserUrl object to a UrlsDetails object.
     * If the UserUrl object is null, returns null.
     * @param url The UserUrl object to be converted to UrlsDetails.
     * @return A UrlsDetails object with details extracted from the UserUrl, or null if the UserUrl is null.
     */
    @Override
    public UrlsDetails userUrlToUserDetails(UserUrl url) {
        // Check if the UserUrl object is null
        if (url == null) {
            return null; // Returns null if the UserUrl object is null
        }
        // Builds and returns a UrlsDetails object using the Builder pattern
        return UrlsDetails
                .builder()
                .id(url.getId()) // Assigns the id from the UserUrl
                .expirationDate(url.getExpirationData()) // Assigns the expiration date from the UserUrl
                .creationDate(url.getCreationData()) // Assigns the creation date from the UserUrl
                .name(url.getName()) // Assigns the name from the UserUrl
                .description(url.getDescription()) // Assigns the description from the UserUrl
                .slug(url.getSlug()) // Assigns the slug from the UserUrl
                .link(url.getLink()) // Assigns the link from the UserUrl
                .build(); // Builds and returns the UrlsDetails object
    }

}
