package ericarfs.socialmedia.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ericarfs.socialmedia.dto.request.user.CreateUserDTO;
import ericarfs.socialmedia.dto.request.user.LoginDTO;
import ericarfs.socialmedia.dto.response.user.UserResponseDTO;
import ericarfs.socialmedia.service.AuthService;
import ericarfs.socialmedia.service.UserService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {
    public final UserService userService;
    public final AuthService authService;

    public AuthController(
            UserService userService,
            AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody CreateUserDTO requestDto) {
        UserResponseDTO response = userService.create(requestDto);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(response.username())
                .toUri())
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            String token = authService.authenticate(loginDTO.username(), loginDTO.password());

            return ResponseEntity.ok().body(Map.of("token", token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
    }

}
