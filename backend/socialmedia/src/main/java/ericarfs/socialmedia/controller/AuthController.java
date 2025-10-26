package ericarfs.socialmedia.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ericarfs.socialmedia.dto.request.user.CreateUserDTO;
import ericarfs.socialmedia.dto.request.user.LoginDTO;
import ericarfs.socialmedia.dto.request.user.ResetPasswordDTO;
import ericarfs.socialmedia.security.JwtUtil;
import ericarfs.socialmedia.service.AuthService;
import ericarfs.socialmedia.service.PasswordResetService;
import ericarfs.socialmedia.service.TokenService;
import ericarfs.socialmedia.service.UserDetailsServiceImpl;
import ericarfs.socialmedia.service.UserService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final TokenService tokenService;
  public final UserService userService;
  public final UserDetailsServiceImpl userDetailsService;
  public final AuthService authService;
  public final PasswordResetService passwordResetService;

  public AuthController(
      UserService userService,
      UserDetailsServiceImpl userDetailsService,
      AuthService authService,
      PasswordResetService emailService,
      PasswordResetService passwordResetService, TokenService tokenService) {
    this.userService = userService;
    this.userDetailsService = userDetailsService;
    this.authService = authService;
    this.passwordResetService = passwordResetService;
    this.tokenService = tokenService;
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(@Valid @RequestBody CreateUserDTO requestDto) {
    String response = userService.create(requestDto);
    return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{username}")
        .buildAndExpand(response)
        .toUri())
        .build();
  }

  @PostMapping("/token")
  public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
    try {
      String accessToken = authService.authenticate(loginDTO.username(), loginDTO.password());

      String refreshToken = JwtUtil.generateRefreshToken(loginDTO.username());
      return ResponseEntity.ok().body(Map.of(
          "access", accessToken,
          "refresh", refreshToken));
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
    }
  }

  @PostMapping("/token/refresh")
  public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
    String refreshToken = request.get("refreshToken");

    try {
      String username = JwtUtil.extractClaims(refreshToken).getSubject();

      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      String newAccessToken = authService.generateToken(userDetails);

      return ResponseEntity.ok().body(Map.of(
          "access", newAccessToken,
          "refresh", refreshToken));
    } catch (Exception e) {
      return ResponseEntity.status(401).body("Invalid refresh token.");
    }
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Void> requestReset(@RequestBody Map<String, String> request) {
    passwordResetService.processRequest(request.get("email"));
    return ResponseEntity.ok().build();
  }

  @GetMapping("/reset-password/{token}")
  public ResponseEntity<String> validateToken(@PathVariable String token) {
    tokenService.find(token);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/reset-password/{token}")
  public ResponseEntity<Void> resetPassword(@PathVariable String token,
      @Valid @RequestBody ResetPasswordDTO request) {
    passwordResetService.resetPassword(token, request.newPassword());
    return ResponseEntity.ok().build();
  }

}
