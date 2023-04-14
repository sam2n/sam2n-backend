package com.sam2n.backend.controller;

import com.sam2n.backend.domain.User;
import com.sam2n.backend.dto.auth.AuthenticateRequest;
import com.sam2n.backend.dto.auth.AuthenticationResponse;
import com.sam2n.backend.dto.auth.RegisterRequest;
import com.sam2n.backend.mapper.RegisterRequestMapper;
import com.sam2n.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//Keep it without service @RestController
//we don't need it. Keep it here for some time because code can be reused
//@RestController
//@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RegisterRequestMapper registerRequestMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest registerRequest
    ) {
        User user = registerRequestMapper.toUser(registerRequest);
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticateRequest authenticateRequest
    ) {
        return ResponseEntity.ok(authService.authenticate(authenticateRequest));
    }
}
