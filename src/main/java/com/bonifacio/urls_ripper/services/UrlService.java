package com.bonifacio.urls_ripper.services;

import com.bonifacio.urls_ripper.dtos.UrlDto;
import com.bonifacio.urls_ripper.dtos.UrlUserDto;
import com.bonifacio.urls_ripper.dtos.UrlsDetails;
import com.bonifacio.urls_ripper.entities.Url;
import com.bonifacio.urls_ripper.entities.UserUrl;

import java.util.UUID;

public interface UrlService {
    public Url generateSlug(UrlDto urlDto);
    public UserUrl generateUserSlug(UrlUserDto urlUserDto,String token);
    public Url persitenstUrl(Url url);
    public UserUrl persitestUserUrl(UserUrl userUrl);
    public Url getEncodeUrl(String url);
    public UrlsDetails getUserUrlById(String id);
    public void deleteSlug(Url url);
    public UrlsDetails updateUserUrl(String id,UrlUserDto userDto);
}
