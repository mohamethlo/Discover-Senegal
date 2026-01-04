package sn.discover.discoversenegal.security.config;

import lombok.RequiredArgsConstructor;
import sn.discover.discoversenegal.entities.User;
import sn.discover.discoversenegal.entities.UserRole;
import sn.discover.discoversenegal.repositories.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .email(adminEmail)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .role(UserRole.ADMIN)
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(admin);
            System.out.println("✅ Compte admin créé : " + adminEmail);
        } else {
            System.out.println("ℹ️ L’admin " + adminEmail + " existe déjà.");
        }
    }
}