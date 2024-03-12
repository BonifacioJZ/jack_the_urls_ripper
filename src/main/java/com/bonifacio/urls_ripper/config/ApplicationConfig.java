package com.bonifacio.urls_ripper.config;

import com.bonifacio.urls_ripper.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepository userRepository;
    /**
     * Creates an AuthenticationManager bean.
     * The `authenticationManager` bean is responsible for authenticating users.
     * Retrieves the AuthenticationManager from the provided AuthenticationConfiguration.
     * @param config The AuthenticationConfiguration used to retrieve the AuthenticationManager.
     * @return The AuthenticationManager bean configured with the provided AuthenticationConfiguration.
     * @throws Exception If an error occurs while retrieving the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Creates an AuthenticationProvider bean.
     * The `authenticationProvider` bean is responsible for providing authentication services.
     * Creates a DaoAuthenticationProvider and configures it with the password encoder and user details service.
     * @return The AuthenticationProvider bean configured with the password encoder and user details service.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Create a DaoAuthenticationProvider
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        // Configure the DaoAuthenticationProvider with the password encoder and user details service
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());

        // Return the configured AuthenticationProvider bean
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }
}
