package com.bonifacio.urls_ripper.services;

import com.bonifacio.urls_ripper.dtos.UrlDto;
import com.bonifacio.urls_ripper.entities.Url;

public interface UrlService {
    public Url generateSlug(UrlDto urlDto);
    public Url persitenstUrl(Url url);
    public Url getEncodeUrl(String url);
    public void deleteSlug(Url url);
}
