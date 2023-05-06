package com.diploma.mindsupport.service;

import com.diploma.mindsupport.dto.AuthenticationRequest;
import com.diploma.mindsupport.dto.AuthenticationResponse;
import com.diploma.mindsupport.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final long EXPIRES_IN_SEC = 60 * 5;
    private final long EXPIRES_IN_MILLI_SEC = EXPIRES_IN_SEC * 1000;

    public AuthenticationResponse register(RegisterRequest request) {
        userService.saveUser(request);
        return AuthenticationResponse.builder()
                .expiresIn(EXPIRES_IN_SEC)
                .token(jwtService.generateToken(request.getUsername(), EXPIRES_IN_MILLI_SEC))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String username = request.getUsername();
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, request.getPassword());
        authenticationManager.authenticate(authentication);

        var user = userDetailsService.loadUserByUsername(username);
        var jwtToken = jwtService.generateToken(user.getUsername(), EXPIRES_IN_MILLI_SEC);
        return AuthenticationResponse.builder()
                .expiresIn(EXPIRES_IN_SEC)
                .token(jwtToken)
                .build();
    }
}
