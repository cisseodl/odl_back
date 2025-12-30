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
			User user = User
					.builder()
					.fullName("Admin ODC")
					.role(Role.ADMIN)
					.email("admin@odc.com")
					.phone("77114120")
					.password(passwordEncoder.encode("77114120"))
					.activate(true)
					.admin(true)
					.build();
			userRepository.save(user);
		}
	}
}
