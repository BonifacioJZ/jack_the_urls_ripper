package com.bonifacio.urls_ripper.encode;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static com.google.common.hash.Hashing.murmur3_32;

@Component
public class UrlEncodeImplements implements UrlEncode {
    /**
     * Encodes a URL string using Murmur3_32 hashing algorithm.
     * The encoding includes concatenating the URL string with the current timestamp
     * before hashing to provide uniqueness.
     * @param url The URL string to be encoded.
     * @return The encoded URL string.
     */
    @Override
    public String urlEncode(String url) {
        String encodeUrl = ""; // Initialize the encoded URL string
        LocalDateTime time = LocalDateTime.now(); // Get the current timestamp
        // Concatenate the URL string with the current timestamp and hash using Murmur3_32
        encodeUrl = murmur3_32()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return encodeUrl; // Return the encoded URL string
    }
}
