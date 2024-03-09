package com.bonifacio.urls_ripper.encode;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static com.google.common.hash.Hashing.murmur3_32;

@Component
public class UrlEncodeImplements implements UrlEncode {
    /**
     * The function takes a URL and encodes it using the Murmur3_32 hashing
     * algorithm along with the
     * current timestamp.
     *
     * @param url The `url` parameter is a string representing the URL that you want
     *            to encode.
     * @return The method is returning the encoded URL as a string.
     */
    @Override
    public String urlEncode(String url) {
        String encodeUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodeUrl = murmur3_32()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return encodeUrl;
    }
}
