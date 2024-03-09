package com.bonifacio.urls_ripper.mappers;

import com.bonifacio.urls_ripper.dtos.UrlDto;
import com.bonifacio.urls_ripper.dtos.UrlUserDto;
import com.bonifacio.urls_ripper.dtos.UrlsDetails;
import com.bonifacio.urls_ripper.entities.Url;
import com.bonifacio.urls_ripper.entities.UserUrl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UrlMapperImplements implements UrlMapper{
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

    @Override
    public UrlsDetails userUrlToUserDetails(Optional<UserUrl> url) {
        if(url.isEmpty()){
            return null;
        }
        return url.map(userUrl -> UrlsDetails
                .builder()
                .id(userUrl.getId())
                .expirationDate(userUrl.getExpirationData())
                .creationDate(userUrl.getCreationData())
                .name(userUrl.getName())
                .description(userUrl.getDescription())
                .slug(userUrl.getSlug())
                .link(userUrl.getLink())
                .build()).orElse(null);
    }

    @Override
    public UrlsDetails userUrlToUserDetails(UserUrl url) {
        if (url==null){
            return null;
        }
        return UrlsDetails
                .builder()
                .id(url.getId())
                .expirationDate(url.getExpirationData())
                .creationDate(url.getCreationData())
                .name(url.getName())
                .description(url.getDescription())
                .slug(url.getSlug())
                .link(url.getLink())
                .build();
    }

}
