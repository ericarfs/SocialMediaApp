package ericarfs.socialmedia.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ericarfs.socialmedia.entity.User;
import ericarfs.socialmedia.entity.enums.Role;
import ericarfs.socialmedia.entity.util.Email;
import ericarfs.socialmedia.repository.UserRepository;
import jakarta.transaction.Transactional;

@Configuration
public class AdminUserConfig implements CommandLineRunner {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
                user -> {
                },
                () -> {
                    var user = new User();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("admin"));
                    user.setEmail(new Email("ericarfscoding@gmail.com"));
                    user.setRole(Role.ADMIN);
                    userRepository.save(user);
                });
    }
}
