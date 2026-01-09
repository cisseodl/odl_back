package com.odc.aws_learning;

// import com.odc.aws_learning.auth.entities.Role; // Removed
import com.odc.aws_learning.auth.entities.Admin;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.AdminRepository;
import com.odc.aws_learning.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Added
public class AwsLearningApplication implements CommandLineRunner {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository; // Injected

    public AwsLearningApplication(UserRepository userRepository, PasswordEncoder passwordEncoder, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(AwsLearningApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Optional<User> userOptional = userRepository.findByEmail("cisseodl@gmail.com");
		if (userOptional.isEmpty()) {
			User user = new User();
			user.setFullName("CisseOdl");
			user.setEmail("cisseodl@gmail.com");
			user.setPhone("0000000000"); // Default phone number
			user.setPassword(passwordEncoder.encode("cisse@2025"));
			user.setActivate(true);

			User savedUser = userRepository.save(user);

            Admin admin = new Admin(savedUser);
            adminRepository.save(admin);
            savedUser.setAdmin(admin);
            userRepository.save(savedUser);
		}
	}
}
