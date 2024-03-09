package com.bonifacio.urls_ripper;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(name = "Jack The Url Ripper",scheme = "Bearer",type = SecuritySchemeType.HTTP,in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(info = @Info(title = "Jack The Url Ripper", version = "0.1",description = "Short Url App"))
public class UrlsRipperApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlsRipperApplication.class, args);
	}

}
