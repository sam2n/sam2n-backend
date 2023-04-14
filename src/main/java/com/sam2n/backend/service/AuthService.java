package com.sam2n.backend.service;

import com.sam2n.backend.domain.User;
import com.sam2n.backend.dto.auth.AuthenticateRequest;
import com.sam2n.backend.dto.auth.AuthenticationResponse;
import com.sam2n.backend.repository.UserRepository;
import com.sam2n.backend.service.security.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

//Keep it without service @Service
//we don't need it. Keep it here for some time because code can be reused
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(User user) {
        user.setLogin(user.getEmail());
        user.setActivated(true);
        user.setCreatedBy(user.getEmail());

        userRepository.save(user);

        return AuthenticationResponse
                .builder()
                .token(jwtService.generateToken(user))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticateRequest authenticateRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticateRequest.getEmail(),
                        authenticateRequest.getPassword()
                )
        );

        UserDetails user = userDetailsService.loadUserByUsername(authenticateRequest.getEmail());

        return AuthenticationResponse
                .builder()
                .token(jwtService.generateToken(user))
                .build();
    }
}
