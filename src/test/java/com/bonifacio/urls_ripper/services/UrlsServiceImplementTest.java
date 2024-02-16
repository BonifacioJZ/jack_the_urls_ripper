package com.bonifacio.urls_ripper.services;

import com.bonifacio.urls_ripper.dtos.UrlDto;
import com.bonifacio.urls_ripper.entities.Url;
import com.bonifacio.urls_ripper.repositories.UrlRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static com.google.common.hash.Hashing.murmur3_32;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UrlsServiceImplementTest {
    @Mock
    private  UrlRepository _urlRepository;
    @InjectMocks
    private UrlsServiceImplement _urlService;
    private Url url;
    private UrlDto urlDto;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
        urlDto = UrlDto.builder()
                .name("slug")
                .url("https://github.com/BonifacioJZ/bmr-app-java/blob/main/src/main/java/com/bonifacio/app/controllers/ProductController.java")
                .build();
        url = Url.builder()
                .name("slug")
                .link("https://github.com/BonifacioJZ/bmr-app-java/blob/main/src/main/java/com/bonifacio/app/controllers/ProductController.java")
                .creationData(LocalDateTime.now())
                .slug(encodeUrl("https://github.com/BonifacioJZ/bmr-app-java/blob/main/src/main/java/com/bonifacio/app/controllers/ProductController.java"))
                .build();
        url.setExpirationData(getExpirationData(urlDto.getExpirationDate(),url.getCreationData()));

    }
    private String encodeUrl(String url) {
        String encodeUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodeUrl = murmur3_32()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return encodeUrl;
    }
    private LocalDateTime getExpirationData(String expirationDate, LocalDateTime creationData) {
        if (StringUtils.isBlank(expirationDate)) {
            return creationData.plusHours(1);
        }
        return LocalDateTime.parse(expirationDate);
    }
    @Test
    void generateSlug() {
        var urlDtoe =UrlDto.builder()
                .name("slug")
                .url("https://github.com/BonifacioJZ/bmr-app-java/blob/main/src/main/java/com/bonifacio/app/controllers/ProductController.java")
                .build();

        var urlRes =_urlService.generateSlug(urlDtoe);
        assertNotNull(urlRes);
        assertEquals(urlRes.getName(),url.getName());

    }

    @Test
    void persitenstUrl() {
        when(_urlRepository.save(any(Url.class))).thenReturn(url);
        var url2 =  Url.builder()
                .name("slug")
                .link("https://github.com/BonifacioJZ/bmr-app-java/blob/main/src/main/java/com/bonifacio/app/controllers/ProductController.java")
                .creationData(LocalDateTime.now())
                .slug(encodeUrl("https://github.com/BonifacioJZ/bmr-app-java/blob/main/src/main/java/com/bonifacio/app/controllers/ProductController.java"))
                .build();
        var result =_urlService.persitenstUrl(url2);
        assertNotNull(result);
        assertEquals(result.getLink(),url.getLink());
        assertEquals(result.getSlug(),url.getSlug());
    }

    @Test
    void getEncodeUrl() {
        when(_urlRepository.findBySlug(any(String.class))).thenReturn(url);
        var result = _urlService.getEncodeUrl(url.getSlug());
        assertNotNull(result);
        assertEquals(result.getName(),url.getName());
        assertEquals(result.getSlug(),url.getSlug());
    }

}