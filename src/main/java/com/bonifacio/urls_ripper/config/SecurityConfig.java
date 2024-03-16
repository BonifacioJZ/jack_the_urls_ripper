package com.bonifacio.urls_ripper.config;

import com.bonifacio.urls_ripper.jwt.JwtAuthenticationFilter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static  org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    // The `securityFilterChain` method is responsible for configuring the security
    // filters and rules
    // for the application.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(antMatcher( "/get/**/"))
                        .permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST,"/"))
                        .permitAll()
                        .requestMatchers(antMatcher("/api/auth/**/")).permitAll()
                        .requestMatchers(antMatcher("/swagger-ui/**/")).permitAll()
                        .requestMatchers(antMatcher("/v3/api-docs/**")).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(
                        httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
    private SecurityScheme createAPIKeyScheme(){
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().addSecurityItem(new SecurityRequirement()
                .addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication",createAPIKeyScheme()))
                .info(new Info().title("Jack The URLS Ripper")
                        .description("App to create Short Links")
                        .version("1.0").contact(new Contact().name("Bonifacio Juarez Ceja")
                                .email("bonijuarezceja96@outlook.com"))
                        .license(new License().name("License MIT")
                                .url("https://github.com/BonifacioJZ/jack_the_urls_ripper/tree/main?tab=MIT-1-ov-file")));


    }
}
//TODO("Refactor autoritation urls")
