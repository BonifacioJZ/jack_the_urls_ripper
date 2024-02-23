package com.bonifacio.urls_ripper.services;

import com.bonifacio.urls_ripper.dtos.UrlsDetails;
import com.bonifacio.urls_ripper.dtos.UserDetails;
import com.bonifacio.urls_ripper.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class UserServiceImplements implements UserService{
    private UserRepository _userRepository;
    private JwtService _jwtService;
    /**
     * @param username 
     * @return
     */
    @Override
    public UserDetails findUser(String username) {
        var user = _userRepository.findByUsername(username);
        if( user.isEmpty()) return null;
        ArrayList<UrlsDetails> urls = new ArrayList<>();
        user.get().getUrls().forEach(url->
                urls.add(new UrlsDetails(
                        url.getId(),
                        url.getName(),
                        url.getDescription(),
                        url.getLink(),
                        url.getSlug(),
                        url.getExpirationData(),
                        url.getCreationData()
                        )));

        return UserDetails.builder()
                .id(user.get().getId())
                .email(user.get().getEmail())
                .urls(urls)
                .firstName(user.get().getFirstName())
                .lastName(user.get().getLastName())
                .username(user.get().getUsername())
                .build();
    }

    /**
     * @param token
     * @return
     */
    @Override
    public UserDetails findUserByToken(String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        var username = _jwtService.getUsernameFromToken(token);
        if(StringUtils.isEmpty(username)){
            return null;
        }
        var user = _userRepository.findByUsername(username).orElseThrow();
        ArrayList<UrlsDetails> urls = new ArrayList<>();
        user.getUrls().forEach(url->
                urls.add(new UrlsDetails(
                        url.getId(),
                        url.getName(),
                        url.getDescription(),
                        url.getLink(),
                        url.getSlug(),
                        url.getExpirationData(),
                        url.getCreationData()
                )));

        return UserDetails
                .builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .lastName(user.getLastName())
                .urls(urls)
                .username(username)
                .build();
    }

}
