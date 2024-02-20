package com.bonifacio.urls_ripper.services;

import com.bonifacio.urls_ripper.dtos.UrlDto;
import com.bonifacio.urls_ripper.entities.Url;
import com.bonifacio.urls_ripper.repositories.UrlRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static com.google.common.hash.Hashing.murmur3_32;

@AllArgsConstructor
@Component
public class UrlsServiceImplement implements UrlService {
    private final UrlRepository _urlRepository;

    /**
     * The `generateSlug` function takes a `UrlDto` object, encodes the URL, creates
     * a new `Url` object
     * with the encoded URL as the slug, sets the creation and expiration dates, and
     * returns the `Url`
     * object.
     * 
     * @param urlDto The `urlDto` parameter is an object of type `UrlDto` which
     *               contains the following
     *               properties:
     * @return The method is returning an object of type Url.
     */
    @Override
    public Url generateSlug(UrlDto urlDto) {
        if (StringUtils.isEmpty(urlDto.url())) {
            return null;
        }
        String encodeUrl = encodeUrl(urlDto.url());
        Url urlPersistence = Url.builder()
                .link(urlDto.url())
                .slug(encodeUrl)
                .creationData(LocalDateTime.now())
                .build();
        urlPersistence
                .setExpirationData(getExpirationData(urlDto.expirationDate(), urlPersistence.getCreationData()));

        if (StringUtils.isEmpty(encodeUrl))
            return null;

        return urlPersistence;
    }

    /**
     * The function returns the expiration date as a LocalDateTime object, either by
     * parsing the
     * provided expiration date string or by adding one hour to the creation date if
     * the expiration
     * date is blank.
     * 
     * @param expirationDate A string representing the expiration date in the format
     *                       "yyyy-MM-dd
     *                       HH:mm:ss".
     * @param creationData   The creationData parameter is a LocalDateTime object
     *                       representing the date
     *                       and time of creation.
     * @return The method is returning a LocalDateTime object.
     */
    private LocalDateTime getExpirationData(String expirationDate, LocalDateTime creationData) {
        if (StringUtils.isBlank(expirationDate)) {
            return creationData.plusHours(1);
        }
        return LocalDateTime.parse(expirationDate);
    }

    /**
     * The function takes a URL and encodes it using the Murmur3_32 hashing
     * algorithm along with the
     * current timestamp.
     * 
     * @param url The `url` parameter is a string representing the URL that you want
     *            to encode.
     * @return The method is returning the encoded URL as a string.
     */
    private String encodeUrl(String url) {
        String encodeUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodeUrl = murmur3_32()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return encodeUrl;
    }

    @Override
    public Url persitenstUrl(Url url) {

        return _urlRepository.save(url);
    }

    @Override
    public Url getEncodeUrl(String url) {
        return _urlRepository.findBySlug(url);
    }

    @Override
    public void deleteSlug(Url url) {
        _urlRepository.delete(url);
    }
}
