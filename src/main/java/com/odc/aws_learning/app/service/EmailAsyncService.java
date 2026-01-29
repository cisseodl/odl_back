package com.odc.aws_learning.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

/**
 * Service pour l'envoi asynchrone d'emails
 * Utilise @Async pour ne pas bloquer les threads principaux
 */
@Service
public class EmailAsyncService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailAsyncService.class);
    private final SendEmailService sendEmailService;
    
    @Autowired
    public EmailAsyncService(SendEmailService sendEmailService) {
        this.sendEmailService = sendEmailService;
    }
    
    /**
     * Envoie un email de manière asynchrone avec retry automatique
     * @param email Email du destinataire
     * @param message Contenu HTML du message
     * @param subject Sujet de l'email
     * @return CompletableFuture qui se complète quand l'email est envoyé
     */
    @Async
    public CompletableFuture<Void> sendEmailAsync(String email, String message, String subject) {
        return sendEmailAsyncWithRetry(email, message, subject, 3);
    }
    
    /**
     * Envoie un email avec retry automatique
     * @param email Email du destinataire
     * @param message Contenu HTML du message
     * @param subject Sujet de l'email
     * @param maxRetries Nombre maximum de tentatives
     * @return CompletableFuture qui se complète quand l'email est envoyé
     */
    @Async
    public CompletableFuture<Void> sendEmailAsyncWithRetry(String email, String message, String subject, int maxRetries) {
        long delayMs = 1000; // 1 seconde initialement
        
        // Vérification préalable
        if (!sendEmailService.isEmailConfigured()) {
            logger.error("❌❌❌ ERREUR: Service email non configuré - Impossible d'envoyer l'email");
            logger.error("Destinataire: {}", email);
            return CompletableFuture.failedFuture(new IllegalStateException("Service email non configuré"));
        }
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                logger.info("========================================");
                logger.info("=== TENTATIVE D'ENVOI D'EMAIL (ASYNC) ===");
                logger.info("Tentative {}/{}", attempt, maxRetries);
                logger.info("Destinataire: {}", email);
                logger.info("Sujet: {}", subject);
                
                sendEmailService.sendEmail(email, message, subject);
                
                logger.info("✅✅✅ Email envoyé avec succès (tentative {}/{})", attempt, maxRetries);
                logger.info("========================================");
                return CompletableFuture.completedFuture(null);
                
            } catch (IllegalStateException | IllegalArgumentException e) {
                // Erreur de configuration ou paramètres invalides - ne pas réessayer
                logger.error("❌❌❌ ERREUR DE CONFIGURATION EMAIL - ARRÊT IMMÉDIAT");
                logger.error("Destinataire: {}", email);
                logger.error("Erreur: {}", e.getMessage(), e);
                return CompletableFuture.failedFuture(e);
                
            } catch (Exception e) {
                // Vérifier si c'est une erreur d'authentification encapsulée
                Throwable cause = e.getCause();
                if (cause instanceof javax.mail.AuthenticationFailedException) {
                    logger.error("❌❌❌ ERREUR D'AUTHENTIFICATION EMAIL - ARRÊT IMMÉDIAT");
                    logger.error("Destinataire: {}", email);
                    logger.error("Vérifiez les identifiants SMTP dans application.properties");
                    logger.error("Erreur: {}", cause.getMessage(), cause);
                    return CompletableFuture.failedFuture(cause);
                }
                
                // Vérifier si c'est directement une AuthenticationFailedException
                if (e instanceof javax.mail.AuthenticationFailedException) {
                    logger.error("❌❌❌ ERREUR D'AUTHENTIFICATION EMAIL - ARRÊT IMMÉDIAT");
                    logger.error("Destinataire: {}", email);
                    logger.error("Vérifiez les identifiants SMTP dans application.properties");
                    logger.error("Erreur: {}", e.getMessage(), e);
                    return CompletableFuture.failedFuture(e);
                }
                logger.error("❌ ERREUR LORS DE L'ENVOI DE L'EMAIL (Tentative {}/{})", attempt, maxRetries);
                logger.error("Destinataire: {}", email);
                logger.error("Type d'erreur: {}", e.getClass().getName());
                logger.error("Message d'erreur: {}", e.getMessage());
                
                if (attempt < maxRetries) {
                    logger.info("⏳ Attente de {}ms avant la prochaine tentative...", delayMs);
                    try {
                        Thread.sleep(delayMs);
                        delayMs *= 2; // Délai exponentiel: 1s, 2s, 4s
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        logger.error("❌ Thread interrompu lors de l'attente", ie);
                        return CompletableFuture.failedFuture(ie);
                    }
                } else {
                    // Dernière tentative échouée
                    logger.error("❌❌❌ ÉCHEC DÉFINITIF DE L'ENVOI DE L'EMAIL APRÈS {} TENTATIVES", maxRetries);
                    logger.error("Destinataire: {}", email, e);
                    return CompletableFuture.failedFuture(e);
                }
            }
        }
        
        return CompletableFuture.failedFuture(new RuntimeException("Échec de l'envoi d'email après " + maxRetries + " tentatives"));
    }
}
