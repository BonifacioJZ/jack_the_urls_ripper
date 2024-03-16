package com.bonifacio.urls_ripper.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {
    @Autowired
    static Environment env;
    ;
    /**
     * Generates a JWT token for the given UserDetails object.
     * Calls the overloaded getToken method with an empty claims map and the UserDetails object.
     * @param user The UserDetails object for which the JWT token will be generated.
     * @return The JWT token generated for the UserDetails object.
     */
    public String getToken(UserDetails user) {
        // Calls the overloaded getToken method with an empty claims map and the UserDetails object
        return getToken(new HashMap<>(), user);
    }

    /**
     * Generates a JWT token for the given UserDetails object with additional claims.
     * @param extraClaims A Map containing additional claims to include in the JWT token.
     * @param user The UserDetails object for which the JWT token will be generated.
     * @return The JWT token generated for the UserDetails object with the specified extra claims.
     */
    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts
                .builder()
                .setClaims(extraClaims) // Sets additional claims provided in the map
                .setSubject(user.getUsername()) // Sets the subject of the token as the username of the user
                .setIssuedAt(new Date(System.currentTimeMillis())) // Sets the token's issued time to the current time
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24)) // Sets token expiration time to 24 hours from now
                .signWith(getKey(), SignatureAlgorithm.HS256) // Signs the token using the specified key and algorithm
                .compact(); // Builds and compact the token
    }
    /**
     * Retrieves the Key used for signing JWT tokens.
     * Decodes the secret key string from Base64 encoding.
     * Returns a Key object generated from the decoded bytes using HMAC SHA algorithm.
     * @return The Key used for signing JWT tokens.
     */
    private Key getKey() {
        // Decode the secret key string from Base64 encoding
        byte[] keyBytes = Decoders.BASE64.decode(Objects.requireNonNull(env.getProperty("SECRET_KEY")));
        // Generates a Key object using HMAC SHA algorithm from the decoded bytes
        return Keys.hmacShaKeyFor(keyBytes);
    }
    /**
     * Extracts the username from a JWT token.
     * Retrieves the subject claim from the token using the specified token parsing function.
     * @param token The JWT token from which to extract the username.
     * @return The username extracted from the JWT token.
     */
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }
    /**
     * Checks if a JWT token is valid for the specified UserDetails.
     * Retrieves the username from the token and compares it with the username from UserDetails.
     * Additionally, checks if the token is not expired.
     * @param token The JWT token to be validated.
     * @param user The UserDetails for which the token validity is checked.
     * @return True if the token is valid for the specified UserDetails, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails user) {
        // Retrieve the username from the token
        final String username = getUsernameFromToken(token);
        // Check if the username extracted from the token matches the username from UserDetails
        // and if the token is not expired
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }
    /**
     * Retrieves all claims from a JWT token.
     * Parses the token, verifies its signature, and returns the body containing all claims.
     * @param token The JWT token from which to retrieve the claims.
     * @return The body of the parsed JWT token containing all claims.
     */
    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder() // Creates a parser builder for parsing JWT tokens
                .setSigningKey(getKey()) // Sets the signing key used for verifying the token's signature
                .build() // Builds the parser
                .parseClaimsJws(token) // Parses the token and verifies its signature
                .getBody(); // Returns the body containing all claims
    }

    /**
     * Retrieves a specific claim from a JWT token using a claims resolver function.
     * Parses the token to retrieve all claims and applies the specified claims resolver function
     * to extract the desired claim.
     * @param token The JWT token from which to retrieve the claim.
     * @param claimsResolver A function to resolve the desired claim from the token's claims.
     * @param <T> The type of the claim to retrieve.
     * @return The resolved claim from the JWT token.
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        // Retrieve all claims from the JWT token
        final Claims claims = getAllClaims(token);
        // Apply the specified claims resolver function to extract the desired claim
        return claimsResolver.apply(claims);
    }

    /**
     * Retrieves the expiration date from a JWT token.
     * Uses the getClaim method to retrieve the expiration claim from the token's claims.
     * @param token The JWT token from which to retrieve the expiration date.
     * @return The expiration date extracted from the JWT token.
     */
    private Date getExpiration(String token) {
        // Retrieve the expiration claim from the token's claims using the getClaim method
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * Checks if a JWT token is expired.
     * Retrieves the expiration date from the token and compares it with the current date.
     * @param token The JWT token to be checked for expiration.
     * @return True if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        // Retrieve the expiration date from the token
        Date expirationDate = getExpiration(token);
        // Compare the expiration date with the current date to check if the token is expired
        return expirationDate.before(new Date());
    }


}