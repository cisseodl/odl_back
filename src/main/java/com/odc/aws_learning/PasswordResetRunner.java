package com.odc.aws_learning;

import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Runner temporaire pour réinitialiser le mot de passe de l'utilisateur admin
 * avec le PasswordEncoder de l'application.
 * 
 * Ce runner garantit que le hash BCrypt en base correspond exactement
 * à l'algorithme utilisé par l'application.
 * 
 * TODO: Désactiver ou supprimer ce runner après avoir vérifié que le login fonctionne.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordResetRunner implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        try {
            // Mot de passe par défaut pour les comptes de démo
            String plainPassword = "password123";

            // Réinitialiser le compte de Mamadou
            resetPasswordForEmail("mamadou.kane@odl.sn",
                    ">>> MOT DE PASSE ADMIN RÉINITIALISÉ AVEC SUCCÈS <<<",
                    plainPassword);

            // Réinitialiser le compte de Awa
            resetPasswordForEmail("awa.diop@odl.sn",
                    ">>> MOT DE PASSE AWA RÉINITIALISÉ <<<",
                    plainPassword);

        } catch (Exception e) {
            log.error("Erreur lors de la réinitialisation des mots de passe: {}", e.getMessage(), e);
        }
    }

    private void resetPasswordForEmail(String email, String successLogMessage, String plainPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            String encodedPassword = passwordEncoder.encode(plainPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);

            log.info("========================================");
            log.info(successLogMessage);
            log.info("Email: {}", email);
            log.info("Nouveau mot de passe: {}", plainPassword);
            log.info("Hash généré: {}", encodedPassword);
            log.info("========================================");
        } else {
            log.warn("Utilisateur avec l'email '{}' non trouvé en base de données. Mot de passe non réinitialisé.", email);
        }
    }
}
