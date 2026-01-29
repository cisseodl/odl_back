package com.odc.aws_learning;

// import com.odc.aws_learning.auth.entities.Role; // Removed
import com.odc.aws_learning.auth.entities.Admin;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.AdminRepository;
import com.odc.aws_learning.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing // Added
@EnableScheduling // Enable scheduled tasks (for lab auto-stop)
@EnableAsync // Enable async methods for email sending
public class AwsLearningApplication implements CommandLineRunner {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository; // Injected
    
    @Autowired(required = false)
    private JavaMailSender javaMailSender;

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
		// Vérification du service email au démarrage
		System.out.println("========================================");
		System.out.println("=== VÉRIFICATION DU SERVICE EMAIL ===");
		if (javaMailSender == null) {
			System.err.println("❌❌❌ ATTENTION: JavaMailSender bean est NULL");
			System.err.println("❌ Les emails ne pourront PAS être envoyés.");
			System.err.println("❌ Vérifiez la configuration dans application.properties:");
			System.err.println("   - spring.mail.enabled=true");
			System.err.println("   - spring.mail.username=...");
			System.err.println("   - spring.mail.password=...");
		} else {
			System.out.println("✅ JavaMailSender bean est disponible");
			System.out.println("✅ Le service d'envoi d'emails est opérationnel");
		}
		System.out.println("========================================");
		
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
