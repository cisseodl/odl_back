package com.odc.aws_learning;

import com.odc.aws_learning.auth.entities.Role;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class AwsLearningApplication implements CommandLineRunner {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

    public AwsLearningApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
		SpringApplication.run(AwsLearningApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Optional<User> userOptional = userRepository.findByEmail("admin@odc.com");
		if (userOptional.isEmpty()) {
			User user = new User();
			user.setFullName("Admin ODC");
			user.setRole(Role.ADMIN);
			user.setEmail("admin@odc.com");
			user.setPhone("77114120");
			user.setPassword(passwordEncoder.encode("77114120"));
			user.setActivate(true);
			user.setAdmin(true);
			userRepository.save(user);
		}
	}
}
