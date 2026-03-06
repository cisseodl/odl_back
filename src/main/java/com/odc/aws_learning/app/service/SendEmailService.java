package com.odc.aws_learning.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;

@Service
public class SendEmailService {
    private static final Logger logger = LoggerFactory.getLogger(SendEmailService.class);
    private final JavaMailSender javaMailSender;
    private static final String FROM_EMAIL = "cisseodl@gmail.com";
    
    public SendEmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        
        // Log au démarrage pour diagnostiquer les problèmes
        if (javaMailSender == null) {
            logger.error("❌❌❌ ATTENTION: JavaMailSender bean est NULL ❌❌❌");
            logger.error("Le service email ne fonctionnera PAS.");
            logger.error("Vérifiez que:");
            logger.error("  1. spring.mail.enabled=true dans application.properties");
            logger.error("  2. spring.mail.username est configuré");
            logger.error("  3. spring.mail.password est configuré");
            logger.error("  4. La classe MailConfig est bien chargée");
        } else {
            logger.info("✅ SendEmailService initialisé avec JavaMailSender disponible");
        }
    }

    /**
     * Vérifie si le service email est configuré et disponible
     * Vérifie que le bean existe ET qu'il est correctement configuré (pas un bean dummy)
     */
    public boolean isEmailConfigured() {
        if (javaMailSender == null) {
            logger.warn("⚠️ isEmailConfigured() retourne FALSE - JavaMailSender est NULL");
            return false;
        }
        
        // Vérifier que ce n'est pas un bean "dummy" (configuration minimale)
        if (javaMailSender instanceof org.springframework.mail.javamail.JavaMailSenderImpl) {
            org.springframework.mail.javamail.JavaMailSenderImpl impl = 
                (org.springframework.mail.javamail.JavaMailSenderImpl) javaMailSender;
            
            // Si le host est "localhost" et le username est vide, c'est un bean dummy
            if ("localhost".equals(impl.getHost()) && 
                (impl.getUsername() == null || impl.getUsername().trim().isEmpty())) {
                logger.warn("⚠️ isEmailConfigured() retourne FALSE - Bean JavaMailSender en mode 'dummy' (email désactivé)");
                return false;
            }
            
            // Vérifier que les credentials sont présents
            if (impl.getUsername() == null || impl.getUsername().trim().isEmpty()) {
                logger.warn("⚠️ isEmailConfigured() retourne FALSE - Username email vide");
                return false;
            }
            
            if (impl.getPassword() == null || impl.getPassword().trim().isEmpty()) {
                logger.warn("⚠️ isEmailConfigured() retourne FALSE - Password email vide");
                return false;
            }
        }
        
        logger.debug("✅ isEmailConfigured() retourne TRUE - Email correctement configuré");
        return true;
    }

    /**
     * Envoie un email de manière synchrone
     * Cette méthode peut être appelée directement ou depuis une méthode asynchrone
     */
    public void sendEmail(String email, String message, String subject) {
        // Vérification préalable stricte
        if (javaMailSender == null) {
            logger.error("❌❌❌ ERREUR CRITIQUE: JavaMailSender est NULL");
            logger.error("❌ Impossible d'envoyer l'email à: {}", email);
            logger.error("❌ Le bean JavaMailSender n'a pas été créé par Spring.");
            logger.error("❌ Vérifiez la configuration dans application.properties:");
            logger.error("   - spring.mail.enabled=true");
            logger.error("   - spring.mail.username=...");
            logger.error("   - spring.mail.password=...");
            throw new IllegalStateException("JavaMailSender bean n'est pas disponible. Vérifiez la configuration email.");
        }
        
        // Vérifier que l'email est réellement configuré (pas un bean dummy)
        if (!isEmailConfigured()) {
            logger.error("❌❌❌ ERREUR CRITIQUE: Email non configuré correctement");
            logger.error("❌ Impossible d'envoyer l'email à: {}", email);
            logger.error("❌ Le bean JavaMailSender existe mais n'est pas correctement configuré.");
            logger.error("❌ Vérifiez la configuration dans application.properties:");
            logger.error("   - spring.mail.enabled=true (doit être 'true')");
            logger.error("   - spring.mail.username=... (doit être défini)");
            logger.error("   - spring.mail.password=... (doit être défini)");
            throw new IllegalStateException("Email non configuré correctement. Vérifiez spring.mail.enabled=true et les credentials.");
        }
        
        if (!StringUtils.hasText(email)) {
            logger.error("❌ Email destinataire vide ou null");
            throw new IllegalArgumentException("Email destinataire invalide");
        }
        
        if (!StringUtils.hasText(message)) {
            logger.error("❌ Message email vide ou null");
            throw new IllegalArgumentException("Message email invalide");
        }
        
        try {
            logger.info("========================================");
            logger.info("=== ENVOI D'EMAIL (SendEmailService) ===");
            logger.info("Destinataire: {}", email);
            logger.info("Sujet: {}", (subject != null ? subject : "Orange Digital Center"));
            logger.info("Longueur du message: {} caractères", (message != null ? message.length() : 0));
            logger.info("From: {}", FROM_EMAIL);
            
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            
            helper.setTo(email);
            helper.setFrom(FROM_EMAIL);
            helper.setSubject(subject != null ? subject : "Orange Digital Center");
            helper.setText(message, true); // true = HTML content
            
            logger.info("Tentative d'envoi de l'email...");
            long startTime = System.currentTimeMillis();
            javaMailSender.send(msg);
            long duration = System.currentTimeMillis() - startTime;
            logger.info("✅✅✅ Email envoyé avec succès à: {} (durée: {}ms)", email, duration);
            logger.info("========================================");

        } catch (javax.mail.AuthenticationFailedException e) {
            logger.error("❌ ERREUR D'AUTHENTIFICATION EMAIL");
            logger.error("Vérifiez les identifiants SMTP dans application.properties");
            logger.error("Email destinataire: {}", email);
            logger.error("Erreur: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur d'authentification email", e);
        } catch (javax.mail.MessagingException e) {
            logger.error("❌ ERREUR DE MESSAGERIE EMAIL");
            logger.error("Vérifiez la configuration SMTP dans application.properties");
            logger.error("Email destinataire: {}", email);
            logger.error("Erreur: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur de messagerie email", e);
        } catch (Exception e) {
            logger.error("❌ ERREUR INATTENDUE LORS DE L'ENVOI D'EMAIL");
            logger.error("Type: {}", e.getClass().getName());
            logger.error("Message: {}", e.getMessage());
            logger.error("Email destinataire: {}", email, e);
            throw new RuntimeException("Erreur lors de l'envoi d'email", e);
        }
    }

    /**
     * Méthode de compatibilité avec l'ancienne API
     */
    public void sendEmailWithAttachment(String email, String message, String subject) {
        sendEmail(email, message, subject);
    }

    public String mailTemplateVerificationCode(String confirmationCode) {
        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 20px; text-align: center\">\n" +
                "  <h1 style=\"color: #5e9ca0;'\">CONNEXION À Orange Digital Center</h1>\n" +
                " <img style=\"height: 100px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/logis-admin.appspot.com/o/icon.png?alt=media&token=597c6a6b-29a9-466a-9d69-fe9dbd52fe52\" alt=\"\">" +
                "  <div style=\"border-bottom: 1px grey solid\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p>Votre code de validation pour Orange Digital Center est le suivant:</p>\n" +
                "<h2 style=\"color: #2e6c80;\">"+confirmationCode+"</h2>\n" +
                "</div>";
    }

    public String mailTemplatePassword(String password) {
        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 20px; text-align: center\">\n" +
                "  <h1 style=\"color: #5e9ca0;'\">CONNEXION À Orange Digital Center</h1>\n" +
                " <img style=\"height: 100px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/odclearning-9bd7a.appspot.com/o/ODLLOGO.png?alt=media&token=1956b05e-3f2d-4885-9a11-df035976b31b\" alt=\"\">" +
                "  <div style=\"border-bottom: 1px grey solid\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p>Votre mot de passe pour Orange Digital Center est le suivant:</p>\n" +
                "<h2 style=\"color: #2e6c80;\">"+password+"</h2>\n" +
                "<br>" +
                "<br>" +
                "<h2 style=\"color: #2e6c80;\"> <i>NB: Veuillez le modifier après la première connexion</i> </h2>\n" +
                "</div>";
    }

    public String mailTemplateWelcome(String fullName, String email) {
        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 20px; text-align: center\">\n" +
                "  <h1 style=\"color: #5e9ca0;'\">BIENVENUE SUR Orange Digital Center</h1>\n" +
                " <img style=\"height: 100px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/odclearning-9bd7a.appspot.com/o/ODLLOGO.png?alt=media&token=1956b05e-3f2d-4885-9a11-df035976b31b\" alt=\"\">" +
                "  <div style=\"border-bottom: 1px grey solid\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p>Bonjour <strong>" + fullName + "</strong>,</p>\n" +
                "<p>Nous sommes ravis de vous accueillir sur Orange Digital Center !</p>\n" +
                "<p>Votre compte a été créé avec succès avec l'adresse email: <strong>" + email + "</strong></p>\n" +
                "<p>Vous pouvez maintenant accéder à la plateforme et commencer votre apprentissage.</p>\n" +
                "<br>" +
                "<p style=\"color: #2e6c80;\">Bonne continuation dans votre parcours d'apprentissage !</p>\n" +
                "</div>";
    }

    public String mailTemplateInstructorCreated(String fullName, String email, String password, String siteUrl) {
        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 30px; text-align: center; font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;\">\n" +
                "  <h1 style=\"color: #FF6600; margin-bottom: 20px; font-size: 24px;\">COMPTE FORMATEUR CRÉÉ - Orange Digital Center</h1>\n" +
                " <img style=\"height: 100px; margin-bottom: 20px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/odclearning-9bd7a.appspot.com/o/ODLLOGO.png?alt=media&token=1956b05e-3f2d-4885-9a11-df035976b31b\" alt=\"Orange Digital Center Logo\">" +
                "  <div style=\"border-bottom: 1px grey solid; margin: 20px 0;\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p style=\"font-size: 16px; margin: 20px 0;\">Bonjour <strong>" + fullName + "</strong>,</p>\n" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Nous sommes ravis de vous accueillir en tant que formateur sur Orange Digital Center !</p>\n" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Votre compte formateur a été créé avec succès. Vous pouvez désormais créer et gérer vos cours, suivre les progrès de vos apprenants et contribuer à leur formation.</p>\n" +
                "<br>" +
                "<div style=\"background-color: #f5f5f5; padding: 20px; border-radius: 5px; margin: 20px 0;\">\n" +
                "<p style=\"font-size: 14px; margin: 10px 0;\"><strong>Vos identifiants de connexion:</strong></p>\n" +
                "<p style=\"font-size: 14px; margin: 10px 0;\">Email: <strong>" + email + "</strong></p>\n" +
                "<p style=\"font-size: 14px; margin: 10px 0;\">Mot de passe: <strong>" + password + "</strong></p>\n" +
                "</div>\n" +
                "<br>" +
                "<p style=\"font-size: 14px; margin: 15px 0;\"><strong>Accédez à votre espace formateur:</strong></p>\n" +
                "<p style=\"margin: 20px 0;\"><a href=\"" + siteUrl + "\" style=\"background-color: #FF6600; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; display: inline-block; font-weight: bold;\">Se connecter</a></p>\n" +
                "<br>" +
                "<p style=\"color: #d32f2f; font-size: 13px; margin: 15px 0; font-weight: bold;\">⚠️ IMPORTANT: Veuillez modifier votre mot de passe après votre première connexion pour des raisons de sécurité.</p>\n" +
                "<br>" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Nous vous souhaitons une excellente expérience en tant que formateur sur notre plateforme.</p>\n" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Bienvenue dans l'équipe des formateurs Orange Digital Center !</p>\n" +
                "<br>" +
                "<p style=\"font-size: 12px; color: #666; margin-top: 30px;\">L'équipe Orange Digital Center</p>\n" +
                "</div>";
    }

    public String mailTemplateInstructorCreatedWithoutPassword(String fullName, String email, String siteUrl) {
        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 30px; text-align: center; font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;\">\n" +
                "  <h1 style=\"color: #FF6600; margin-bottom: 20px; font-size: 24px;\">COMPTE FORMATEUR CRÉÉ - Orange Digital Center</h1>\n" +
                " <img style=\"height: 100px; margin-bottom: 20px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/odclearning-9bd7a.appspot.com/o/ODLLOGO.png?alt=media&token=1956b05e-3f2d-4885-9a11-df035976b31b\" alt=\"Orange Digital Center Logo\">" +
                "  <div style=\"border-bottom: 1px grey solid; margin: 20px 0;\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p style=\"font-size: 16px; margin: 20px 0;\">Bonjour <strong>" + fullName + "</strong>,</p>\n" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Nous sommes ravis de vous accueillir en tant que formateur sur Orange Digital Center !</p>\n" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Votre compte formateur a été créé avec succès. Vous pouvez désormais créer et gérer vos cours, suivre les progrès de vos apprenants et contribuer à leur formation.</p>\n" +
                "<br>" +
                "<div style=\"background-color: #f5f5f5; padding: 20px; border-radius: 5px; margin: 20px 0;\">\n" +
                "<p style=\"font-size: 14px; margin: 10px 0;\"><strong>Vos identifiants de connexion:</strong></p>\n" +
                "<p style=\"font-size: 14px; margin: 10px 0;\">Email: <strong>" + email + "</strong></p>\n" +
                "<p style=\"font-size: 14px; margin: 10px 0;\">Utilisez votre mot de passe existant pour vous connecter.</p>\n" +
                "</div>\n" +
                "<br>" +
                "<p style=\"font-size: 14px; margin: 15px 0;\"><strong>Accédez à votre espace formateur:</strong></p>\n" +
                "<p style=\"margin: 20px 0;\"><a href=\"" + siteUrl + "\" style=\"background-color: #FF6600; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; display: inline-block; font-weight: bold;\">Se connecter</a></p>\n" +
                "<br>" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Nous vous souhaitons une excellente expérience en tant que formateur sur notre plateforme.</p>\n" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Bienvenue dans l'équipe des formateurs Orange Digital Center !</p>\n" +
                "<br>" +
                "<p style=\"font-size: 12px; color: #666; margin-top: 30px;\">L'équipe Orange Digital Center</p>\n" +
                "</div>";
    }

    public String mailTemplateAdminCreated(String fullName, String email, String siteUrl) {
        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 20px; text-align: center\">\n" +
                "  <h1 style=\"color: #5e9ca0;'\">COMPTE ADMINISTRATEUR CRÉÉ - Orange Digital Center</h1>\n" +
                " <img style=\"height: 100px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/odclearning-9bd7a.appspot.com/o/ODLLOGO.png?alt=media&token=1956b05e-3f2d-4885-9a11-df035976b31b\" alt=\"\">" +
                "  <div style=\"border-bottom: 1px grey solid\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p>Bonjour <strong>" + fullName + "</strong>,</p>\n" +
                "<p>Votre compte administrateur a été créé avec succès sur Orange Digital Center !</p>\n" +
                "<p><strong>Vos identifiants de connexion:</strong></p>\n" +
                "<p>Email: <strong>" + email + "</strong></p>\n" +
                "<br>" +
                "<p><strong>Lien de connexion:</strong></p>\n" +
                "<p><a href=\"" + siteUrl + "\" style=\"color: #2e6c80; text-decoration: underline;\">" + siteUrl + "</a></p>\n" +
                "<br>" +
                "<p>Bienvenue dans l'équipe d'administration !</p>\n" +
                "</div>";
    }

    public String mailTemplateApprenantCreated(String fullName, String email, String siteUrl) {
        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 20px; text-align: center; font-family: Arial, sans-serif;\">\n" +
                "  <h1 style=\"color: #FF6600; margin-bottom: 20px;\">Bienvenue sur Orange Digital Center</h1>\n" +
                " <img style=\"height: 100px; margin-bottom: 20px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/odclearning-9bd7a.appspot.com/o/ODLLOGO.png?alt=media&token=1956b05e-3f2d-4885-9a11-df035976b31b\" alt=\"Orange Digital Center Logo\">" +
                "  <div style=\"border-bottom: 1px grey solid; margin: 20px 0;\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p style=\"font-size: 16px; margin: 20px 0;\">Bonjour <strong>" + fullName + "</strong>,</p>\n" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Nous sommes ravis de vous accueillir sur Orange Digital Center !</p>\n" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Votre compte apprenant a été créé avec succès.</p>\n" +
                "<br>" +
                "<div style=\"background-color: #f5f5f5; padding: 15px; border-radius: 5px; margin: 20px 0;\">\n" +
                "<p style=\"font-size: 14px; margin: 10px 0;\"><strong>Vos identifiants de connexion:</strong></p>\n" +
                "<p style=\"font-size: 14px; margin: 10px 0;\">Email: <strong>" + email + "</strong></p>\n" +
                "</div>\n" +
                "<br>" +
                "<p style=\"font-size: 14px; margin: 15px 0;\"><strong>Accédez à votre espace d'apprentissage:</strong></p>\n" +
                "<p style=\"margin: 20px 0;\"><a href=\"" + siteUrl + "\" style=\"background-color: #FF6600; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; display: inline-block; font-weight: bold;\">Se connecter</a></p>\n" +
                "<br>" +
                "<p style=\"font-size: 14px; margin: 15px 0; color: #666;\">Vous pouvez maintenant explorer nos cours, suivre votre progression et obtenir des certificats.</p>\n" +
                "<br>" +
                "<p style=\"font-size: 14px; margin: 15px 0; color: #FF6600;\"><strong>Bienvenue dans votre parcours d'apprentissage !</strong></p>\n" +
                "<br>" +
                "<p style=\"font-size: 12px; color: #999; margin-top: 30px;\">L'équipe Orange Digital Center</p>\n" +
                "</div>";
    }

    /**
     * Template d'email pour apprenant créé par un admin avec mot de passe par défaut
     */
    public String mailTemplateApprenantCreatedWithPassword(String fullName, String email, String password, String siteUrl) {
        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 30px; text-align: center; font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;\">\n" +
                "  <h1 style=\"color: #FF6600; margin-bottom: 20px; font-size: 24px;\">Bienvenue sur Orange Digital Center</h1>\n" +
                " <img style=\"height: 100px; margin-bottom: 20px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/odclearning-9bd7a.appspot.com/o/ODLLOGO.png?alt=media&token=1956b05e-3f2d-4885-9a11-df035976b31b\" alt=\"Orange Digital Center Logo\">" +
                "  <div style=\"border-bottom: 1px grey solid; margin: 20px 0;\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p style=\"font-size: 16px; margin: 20px 0;\">Bonjour <strong>" + fullName + "</strong>,</p>\n" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Nous sommes ravis de vous accueillir sur Orange Digital Center !</p>\n" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Votre compte apprenant a été créé avec succès. Vous pouvez désormais accéder à tous nos cours et commencer votre parcours d'apprentissage.</p>\n" +
                "<br>" +
                "<div style=\"background-color: #f5f5f5; padding: 20px; border-radius: 5px; margin: 20px 0;\">\n" +
                "<p style=\"font-size: 14px; margin: 10px 0;\"><strong>Vos identifiants de connexion:</strong></p>\n" +
                "<p style=\"font-size: 14px; margin: 10px 0;\">Email: <strong>" + email + "</strong></p>\n" +
                "<p style=\"font-size: 14px; margin: 10px 0;\">Mot de passe: <strong>" + password + "</strong></p>\n" +
                "</div>\n" +
                "<br>" +
                "<p style=\"font-size: 14px; margin: 15px 0;\"><strong>Accédez à votre espace d'apprentissage:</strong></p>\n" +
                "<p style=\"margin: 20px 0;\"><a href=\"" + siteUrl + "\" style=\"background-color: #FF6600; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; display: inline-block; font-weight: bold;\">Se connecter</a></p>\n" +
                "<br>" +
                "<p style=\"color: #d32f2f; font-size: 13px; margin: 15px 0; font-weight: bold;\">⚠️ IMPORTANT: Veuillez modifier votre mot de passe après votre première connexion pour des raisons de sécurité.</p>\n" +
                "<br>" +
                "<p style=\"font-size: 14px; margin: 15px 0; color: #666;\">Vous pouvez maintenant explorer nos cours, suivre votre progression et obtenir des certificats.</p>\n" +
                "<br>" +
                "<p style=\"font-size: 14px; margin: 15px 0; color: #FF6600;\"><strong>Bienvenue dans votre parcours d'apprentissage !</strong></p>\n" +
                "<br>" +
                "<p style=\"font-size: 12px; color: #666; margin-top: 30px;\">L'équipe Orange Digital Center</p>\n" +
                "</div>";
    }

    /**
     * Envoie un email de félicitations à l'apprenant pour l'obtention du certificat (validation des labs par l'instructeur).
     */
    public void sendCertificateCongratulationsByLabs(String email, String fullName, String courseTitle) {
        String subject = "Félicitations ! Vous avez obtenu votre certificat - " + courseTitle;
        String message = mailTemplateCertificateCongratulationsByLabs(fullName, courseTitle);
        sendEmail(email, message, subject);
    }

    public String mailTemplateCertificateCongratulationsByLabs(String fullName, String courseTitle) {
        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 20px; text-align: center; font-family: Arial, sans-serif;\">\n" +
                "  <h1 style=\"color: #FF6600; margin-bottom: 20px;\">Félicitations !</h1>\n" +
                " <img style=\"height: 100px; margin-bottom: 20px;\" src=\"https://firebasestorage.googleapis.com/v0/b/odclearning-9bd7a.appspot.com/o/ODLLOGO.png?alt=media&token=1956b05e-3f2d-4885-9a11-df035976b31b\" alt=\"Orange Digital Center\">" +
                "  <div style=\"border-bottom: 1px grey solid; margin: 20px 0;\"></div>\n" +
                "<p style=\"font-size: 16px; margin: 20px 0;\">Bonjour <strong>" + (fullName != null ? fullName : "Apprenant") + "</strong>,</p>\n" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Vous avez correctement réalisé tous les labs du cours <strong>" + (courseTitle != null ? courseTitle : "cours") + "</strong>.</p>\n" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Votre instructeur a validé votre travail. Vous méritez votre certificat !</p>\n" +
                "<p style=\"font-size: 14px; margin: 15px 0; color: #2e6c80;\"><strong>Votre certificat est disponible dans votre espace profil (onglet Certificats).</strong></p>\n" +
                "<br><p style=\"font-size: 12px; color: #999; margin-top: 30px;\">L'équipe Orange Digital Center</p>\n" +
                "</div>";
    }
}





//package com.odc.aws_learning.app.service;
//
//        import org.springframework.beans.factory.annotation.Autowired;
//        import org.springframework.mail.javamail.JavaMailSender;
//        import org.springframework.mail.javamail.MimeMessageHelper;
//        import org.springframework.stereotype.Service;
//
//        import javax.mail.internet.MimeMessage;
//
//@Service
//public class SendEmailService {
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    public void sendEmailWithAttachment(String email, String message, String subject) {
//        try {
//            System.out.println("start sending...");
//            MimeMessage msg = javaMailSender.createMimeMessage();
//
//            // true = multipart message
//            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
////            helper.setTo(applicationUser.getUsername());
//            helper.setTo(new String[]{email});
//
//            helper.setSubject(subject);
//
//            // default = text/plain
//            //helper.setText("Check attachment for image!");
//
//            helper.setText(mailTemplatePassword(message), true);
//            javaMailSender.send(msg);
//            System.out.println("end sending...");
//
//        } catch (Exception e) {
//            e.printStackTrace(System.out);
//        }
//    }
//
//    public String mailTemplatePassword(String confirmationCode) {
//        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 20px; text-align: center\">\n" +
//                "  <h1 style=\"color: #5e9ca0;'\">CONNEXION À Orange Digital Center</h1>\n" +
//                " <img style=\"height: 100px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/logis-admin.appspot.com/o/icon.png?alt=media&token=597c6a6b-29a9-466a-9d69-fe9dbd52fe52\" alt=\"\">" +
//                "  <div style=\"border-bottom: 1px grey solid\">\n" +
//                "    \n" +
//                "  </div>\n" +
//                "\n" +
//                "<p>Votre code de validation pour Orange Digital Center est le suivant:</p>\n" +
//                "<h2 style=\"color: #2e6c80;\">"+confirmationCode+"</h2>\n" +
//                "<p>Veuillez le modifier après la première connexion.</p>\n" +
//                "</div>";
//    }
//}
