package com.odc.aws_learning;

// import com.odc.aws_learning.auth.entities.Role; // Removed
import com.odc.aws_learning.auth.entities.Admin;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.AdminRepository;
import com.odc.aws_learning.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(AwsLearningApplication.class);
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
		logger.info("========================================");
		logger.info("=== VÉRIFICATION DU SERVICE EMAIL ===");
		if (javaMailSender == null) {
			logger.error("❌❌❌ ATTENTION: JavaMailSender bean est NULL");
			logger.error("❌ Les emails ne pourront PAS être envoyés.");
			logger.error("❌ Vérifiez la configuration dans application.properties:");
			logger.error("   - spring.mail.enabled=true");
			logger.error("   - spring.mail.username=...");
			logger.error("   - spring.mail.password=...");
		} else {
			logger.info("✅ JavaMailSender bean est disponible");
			logger.info("✅ Le service d'envoi d'emails est opérationnel");
		}
		logger.info("========================================");

		try {
			logger.info("========================================");
			logger.info("Vérification de l'utilisateur admin...");
			logger.info("========================================");

			Optional<User> userOptional = userRepository.findByEmail("cisseodl@gmail.com");
			if (userOptional.isEmpty()) {
				logger.info("Utilisateur admin non trouvé. Création en cours...");

				User user = new User();
				user.setFullName("CisseOdl");
				user.setEmail("cisseodl@gmail.com");
				user.setPhone("0000000000"); // Default phone number
				user.setPassword(passwordEncoder.encode("cisse@2025"));
				user.setActivate(true);

				User savedUser = userRepository.save(user);
				logger.info("✅ Utilisateur créé avec ID: {}", savedUser.getId());

				Admin admin = new Admin(savedUser);
				Admin savedAdmin = adminRepository.save(admin);
				logger.info("✅ Admin créé avec ID: {}", savedAdmin.getId());

				savedUser.setAdmin(savedAdmin);
				userRepository.save(savedUser);
				logger.info("✅ Relation User-Admin configurée");

				logger.info("========================================");
				logger.info("✅ UTILISATEUR ADMIN CRÉÉ AVEC SUCCÈS");
				logger.info("Email: cisseodl@gmail.com");
				logger.info("Mot de passe: cisse@2025");
				logger.info("========================================");
			} else {
				logger.info("✅ Utilisateur admin existe déjà: {}", userOptional.get().getEmail());
			}
		} catch (Exception e) {
			logger.error("❌ ERREUR lors de la création de l'utilisateur admin: {}", e.getMessage(), e);
		}
	}
}
