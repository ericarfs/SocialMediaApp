package ericarfs.socialmedia.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.stereotype.Service;

import ericarfs.socialmedia.entity.ResetToken;
import ericarfs.socialmedia.entity.User;
import ericarfs.socialmedia.exceptions.ResourceNotFoundException;
import ericarfs.socialmedia.repository.ResetTokenRepository;

@Service
public class TokenService {
    private final ResetTokenRepository resetTokenRepository;

    private static final SecureRandom secureRandom = new SecureRandom();

    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    public TokenService(ResetTokenRepository resetTokenRepository) {
        this.resetTokenRepository = resetTokenRepository;
    }

    public void create(User user, String token) {
        String hashedToken = hashToken(token);

        ResetToken record = new ResetToken();
        record.setToken(hashedToken);
        record.setUser(user);
        record.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        resetTokenRepository.save(record);
    }

    public String makeResetToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public ResetToken find(String token) {
        String hashedToken = hashToken(token);

        ResetToken resetToken = resetTokenRepository.findByToken(hashedToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found."));

        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("Token is invalid.");
        }

        return resetToken;
    }

    public String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
