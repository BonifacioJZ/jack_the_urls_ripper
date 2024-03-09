package com.bonifacio.urls_ripper.mappers;

import com.bonifacio.urls_ripper.dtos.UrlDto;
import com.bonifacio.urls_ripper.dtos.UrlUserDto;
import com.bonifacio.urls_ripper.encode.UrlEncode;
import com.bonifacio.urls_ripper.entities.Url;
import com.bonifacio.urls_ripper.entities.UserUrl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class UrlMapperImplements implements  UrlMapper{
    @Override
    public Url urlDtoToUrl(UrlDto urlDto) {
        if(urlDto== null){
            return null;
        }
        return Url.builder()
                .creationData(LocalDateTime.now())
                .link(urlDto.url())
                .build();
    }

    @Override
    public UserUrl urlUserDtoToUserUrl(UrlUserDto urlUserDto) {
        if(urlUserDto == null){
            return null;
        }
        return UserUrl.builder()
                .name(urlUserDto.name())
                .link(urlUserDto.url())
                .description(urlUserDto.description())
                .creationData(LocalDateTime.now())
                .build();
    }
}
