package com.bonifacio.urls_ripper.services;

import com.bonifacio.urls_ripper.dtos.LoginDto;
import com.bonifacio.urls_ripper.dtos.RegisterDto;
import com.bonifacio.urls_ripper.entities.User;
import com.bonifacio.urls_ripper.mappers.UserMapper;
import com.bonifacio.urls_ripper.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthServiceImplement implements AuthService{
    @Autowired
    private final UserRepository _userRepository;
    @Autowired
    private final JwtService _jwtService;
    @Autowired
    private final PasswordEncoder _passwordEncoder;
    @Autowired
    private final AuthenticationManager _authenticationManager;
    private final UserMapper _userMapper;


    /**
     * @param loginDto 
     * @return
     */
    @Override
    public String login(LoginDto loginDto) {
        _authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.username(),
                loginDto.password()
        ));
        UserDetails user = _userRepository.findByUsername(loginDto.username()).orElseThrow();
        return _jwtService.getToken(user);
    }

    /**
     * @param registerDto 
     * @return
     */
    @Override
    public String register(RegisterDto registerDto) {
        var user = _userMapper.registerDtoToUser(registerDto);
        user.setPassword(_passwordEncoder.encode(registerDto.password()));
        user = _userRepository.save(user);
        return _jwtService.getToken(user);
    }
}
