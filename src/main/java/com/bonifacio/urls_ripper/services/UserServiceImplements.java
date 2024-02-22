package com.bonifacio.urls_ripper.services;

import com.bonifacio.urls_ripper.dtos.UrlsDetails;
import com.bonifacio.urls_ripper.dtos.UserDetails;
import com.bonifacio.urls_ripper.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class UserServiceImplements implements UserService{
    private UserRepository _userRepository;
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
}
