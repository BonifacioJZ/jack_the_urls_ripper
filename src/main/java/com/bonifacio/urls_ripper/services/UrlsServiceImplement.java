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
public class UrlsServiceImplement implements  UrlService{
    private final UrlRepository _urlRepository;
    @Override
    public Url generateSlug(UrlDto urlDto) {
        if(StringUtils.isEmpty(urlDto.getUrl())) {
            return null;
        }
        String encodeUrl = encodeUrl(urlDto.getUrl());
        Url urlPersistence = Url.builder()
                .name(urlDto.getName())
                .description(urlDto.getDescription())
                .link(urlDto.getUrl())
                .slug(encodeUrl)
                .creationData(LocalDateTime.now())
                .build();
        urlPersistence.setExpirationData(getExpirationData(urlDto.getExpirationDate(),urlPersistence.getCreationData()));


        return null;
    }

    private LocalDateTime getExpirationData(String expirationDate, LocalDateTime creationData) {
        if(StringUtils.isBlank(expirationDate)){
            return creationData.plusHours(1);
        }
        return LocalDateTime.parse(expirationDate);
    }

    private String encodeUrl(String url) {
        String encodeUrl ="";
        LocalDateTime time = LocalDateTime.now();
        encodeUrl = murmur3_32()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return  encodeUrl;
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

    }
}
