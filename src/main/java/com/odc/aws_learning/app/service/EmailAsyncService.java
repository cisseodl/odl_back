package com.odc.aws_learning.app.service;

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
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                System.out.println("=== TENTATIVE D'ENVOI D'EMAIL (ASYNC) ===");
                System.out.println("Tentative " + attempt + "/" + maxRetries);
                System.out.println("Destinataire: " + email);
                
                sendEmailService.sendEmail(email, message, subject);
                
                System.out.println("✅ Email envoyé avec succès (tentative " + attempt + "/" + maxRetries + ")");
                return CompletableFuture.completedFuture(null);
                
            } catch (Exception e) {
                System.err.println("❌ ERREUR LORS DE L'ENVOI DE L'EMAIL (Tentative " + attempt + "/" + maxRetries + ")");
                System.err.println("Destinataire: " + email);
                System.err.println("Type d'erreur: " + e.getClass().getName());
                System.err.println("Message d'erreur: " + e.getMessage());
                
                if (attempt < maxRetries) {
                    System.out.println("⏳ Attente de " + delayMs + "ms avant la prochaine tentative...");
                    try {
                        Thread.sleep(delayMs);
                        delayMs *= 2; // Délai exponentiel: 1s, 2s, 4s
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        System.err.println("❌ Thread interrompu lors de l'attente");
                        return CompletableFuture.failedFuture(ie);
                    }
                } else {
                    // Dernière tentative échouée
                    System.err.println("❌ ÉCHEC DÉFINITIF DE L'ENVOI DE L'EMAIL APRÈS " + maxRetries + " TENTATIVES");
                    e.printStackTrace(System.err);
                    return CompletableFuture.failedFuture(e);
                }
            }
        }
        
        return CompletableFuture.failedFuture(new RuntimeException("Échec de l'envoi d'email après " + maxRetries + " tentatives"));
    }
}
