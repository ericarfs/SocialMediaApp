package ericarfs.socialmedia.service;

import java.util.Optional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ericarfs.socialmedia.entity.ResetToken;
import ericarfs.socialmedia.entity.User;
import ericarfs.socialmedia.entity.util.Email;
import ericarfs.socialmedia.exceptions.ResourceConflictException;
import ericarfs.socialmedia.exceptions.ResourceNotFoundException;
import ericarfs.socialmedia.repository.ResetTokenRepository;
import ericarfs.socialmedia.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class PasswordResetService {
    private final UserRepository userRepository;
    private final ResetTokenRepository resetTokenRepository;

    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;

    public PasswordResetService(
            UserRepository userRepository,
            ResetTokenRepository resetTokenRepository,
            TokenService tokenService,
            JavaMailSender mailSender,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.tokenService = tokenService;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void processRequest(String email) {
        Optional<User> userObj = userRepository.findByEmail(new Email(email));
        if (userObj.isEmpty())
            return;

        User user = userObj.get();

        resetTokenRepository.findByUser(user).ifPresent(existing -> {
            if (!existing.isExpired()) {
                throw new ResourceConflictException("An active token already exists. Please check your email.");
            }
            resetTokenRepository.delete(existing);
        });

        String token = tokenService.makeResetToken();

        tokenService.create(user, token);

        sendEmail(email, user.getUsername(), token);
    }

    public void sendEmail(String email, String username, String token) {
        String resetUrl = "http://localhost:4200/reset-password/" + token;

        String body = """
                    Hi, %s!\n\n
                    We received a request to reset your MaritaQA password.
                    Click the link below to proceed.\n\n
                    %s
                """.formatted(username, resetUrl);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset");
        message.setText(body);

        mailSender.send(message);
    }

    @Transactional
    public void resetPassword(String requestToken, String newPassword) {
        ResetToken token = tokenService.find(requestToken);

        User user = userRepository.findById(token.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);

        user.setResetToken(null);

        userRepository.save(user);
        resetTokenRepository.deleteById(token.getId());
    }
}
