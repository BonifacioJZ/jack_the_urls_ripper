package com.bonifacio.urls_ripper.services;

import com.bonifacio.urls_ripper.dtos.UrlsDetails;
import com.bonifacio.urls_ripper.dtos.UserDetails;
import com.bonifacio.urls_ripper.mappers.UrlMapper;
import com.bonifacio.urls_ripper.mappers.UserMapper;
import com.bonifacio.urls_ripper.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class UserServiceImplements implements UserService{
    @Autowired
    private UserRepository _userRepository;
    @Autowired
    private JwtService _jwtService;
    @Autowired
    private final UrlMapper _urlMapper;
    @Autowired
    private final UserMapper _userMapper;
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
                urls.add(_urlMapper.userUrlToUserDetails(url))
        );
        return _userMapper.userToUserDetails(user,urls);
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
                urls.add(
                        _urlMapper.userUrlToUserDetails(url)
                ));

        return _userMapper.userToUserDetails(user,urls);
    }

}
