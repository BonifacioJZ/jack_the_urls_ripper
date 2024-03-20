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
                        .requestMatchers(antMatcher( "/get/**"))
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
    /**
     * Configures the OpenAPI documentation for the application.
     *
     * @return An instance of OpenAPI containing security requirements, components, and general information about the API.
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                // Adds a security requirement for Bearer Authentication
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                // Adds security schemes for Bearer Authentication
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                // Provides general information about the application
                .info(new Info()
                        .title("Jack The URLS Ripper") // Title of the application
                        .description("App to create Short Links") // Description of the application
                        .version("1.0") // Version of the application
                        .contact(new Contact()
                                .name("Bonifacio Juarez Ceja") // Name of the contact person
                                .email("bonijuarezceja96@outlook.com")) // Email of the contact person
                        .license(new License()
                                .name("License MIT") // Name of the license
                                .url("https://github.com/BonifacioJZ/jack_the_urls_ripper/tree/main?tab=MIT-1-ov-file"))); // URL of the license
    }

}
