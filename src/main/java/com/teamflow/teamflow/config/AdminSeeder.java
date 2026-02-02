package com.teamflow.teamflow.config;

import com.teamflow.teamflow.model.Role;
import com.teamflow.teamflow.model.User;
import com.teamflow.teamflow.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AdminSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@teamflow.com").isEmpty()) {

            User admin = new User();
            admin.setEmail("admin@teamflow.com");
            admin.setPassword(encoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            admin.setFirstLogin(false);
            admin.setLoginCount(0);
            admin.setName("admin");

            userRepository.save(admin);

            System.out.println("✅ Admin user seeded");
        }
    }
}
