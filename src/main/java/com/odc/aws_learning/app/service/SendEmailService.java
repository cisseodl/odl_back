package com.odc.aws_learning.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;

@Service
public class SendEmailService {
    private final JavaMailSender javaMailSender;
    private static final String FROM_EMAIL = "cisseodl@gmail.com";
    
    @Autowired(required = false)
    public SendEmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        
        // Log au démarrage pour diagnostiquer les problèmes
        if (javaMailSender == null) {
            System.err.println("❌❌❌ ATTENTION: JavaMailSender bean est NULL ❌❌❌");
            System.err.println("Le service email ne fonctionnera PAS.");
            System.err.println("Vérifiez que:");
            System.err.println("  1. spring.mail.enabled=true dans application.properties");
            System.err.println("  2. spring.mail.username est configuré");
            System.err.println("  3. spring.mail.password est configuré");
            System.err.println("  4. La classe MailConfig est bien chargée");
        } else {
            System.out.println("✅ SendEmailService initialisé avec JavaMailSender disponible");
        }
    }

    /**
     * Vérifie si le service email est configuré et disponible
     */
    public boolean isEmailConfigured() {
        boolean configured = javaMailSender != null;
        if (!configured) {
            System.err.println("⚠️ isEmailConfigured() retourne FALSE - JavaMailSender est NULL");
        }
        return configured;
    }

    /**
     * Envoie un email de manière synchrone
     * Cette méthode peut être appelée directement ou depuis une méthode asynchrone
     */
    public void sendEmail(String email, String message, String subject) {
        // Vérification préalable stricte
        if (javaMailSender == null) {
            System.err.println("❌❌❌ ERREUR CRITIQUE: JavaMailSender est NULL");
            System.err.println("❌ Impossible d'envoyer l'email à: " + email);
            System.err.println("❌ Le bean JavaMailSender n'a pas été créé par Spring.");
            System.err.println("❌ Vérifiez la configuration dans application.properties:");
            System.err.println("   - spring.mail.enabled=true");
            System.err.println("   - spring.mail.username=...");
            System.err.println("   - spring.mail.password=...");
            throw new IllegalStateException("JavaMailSender bean n'est pas disponible. Vérifiez la configuration email.");
        }
        
        if (!StringUtils.hasText(email)) {
            System.err.println("❌ Email destinataire vide ou null");
            throw new IllegalArgumentException("Email destinataire invalide");
        }
        
        if (!StringUtils.hasText(message)) {
            System.err.println("❌ Message email vide ou null");
            throw new IllegalArgumentException("Message email invalide");
        }
        
        try {
            System.out.println("========================================");
            System.out.println("=== ENVOI D'EMAIL (SendEmailService) ===");
            System.out.println("Destinataire: " + email);
            System.out.println("Sujet: " + (subject != null ? subject : "Orange Digital Learning"));
            System.out.println("Longueur du message: " + (message != null ? message.length() : 0) + " caractères");
            System.out.println("From: " + FROM_EMAIL);
            
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            
            helper.setTo(email);
            helper.setFrom(FROM_EMAIL);
            helper.setSubject(subject != null ? subject : "Orange Digital Learning");
            helper.setText(message, true); // true = HTML content
            
            System.out.println("Tentative d'envoi de l'email...");
            long startTime = System.currentTimeMillis();
            javaMailSender.send(msg);
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("✅✅✅ Email envoyé avec succès à: " + email + " (durée: " + duration + "ms)");
            System.out.println("========================================");

        } catch (javax.mail.AuthenticationFailedException e) {
            System.err.println("❌ ERREUR D'AUTHENTIFICATION EMAIL");
            System.err.println("Vérifiez les identifiants SMTP dans application.properties");
            System.err.println("Email destinataire: " + email);
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Erreur d'authentification email", e);
        } catch (javax.mail.MessagingException e) {
            System.err.println("❌ ERREUR DE MESSAGERIE EMAIL");
            System.err.println("Vérifiez la configuration SMTP dans application.properties");
            System.err.println("Email destinataire: " + email);
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace(System.err);
            throw new RuntimeException("Erreur de messagerie email", e);
        } catch (Exception e) {
            System.err.println("❌ ERREUR INATTENDUE LORS DE L'ENVOI D'EMAIL");
            System.err.println("Type: " + e.getClass().getName());
            System.err.println("Message: " + e.getMessage());
            System.err.println("Email destinataire: " + email);
            e.printStackTrace(System.err);
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
                "  <h1 style=\"color: #5e9ca0;'\">CONNEXION À Orange Digital Learning</h1>\n" +
                " <img style=\"height: 100px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/logis-admin.appspot.com/o/icon.png?alt=media&token=597c6a6b-29a9-466a-9d69-fe9dbd52fe52\" alt=\"\">" +
                "  <div style=\"border-bottom: 1px grey solid\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p>Votre code de validation pour Orange Digital Learning est le suivant:</p>\n" +
                "<h2 style=\"color: #2e6c80;\">"+confirmationCode+"</h2>\n" +
                "</div>";
    }

    public String mailTemplatePassword(String password) {
        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 20px; text-align: center\">\n" +
                "  <h1 style=\"color: #5e9ca0;'\">CONNEXION À Orange Digital Learning</h1>\n" +
                " <img style=\"height: 100px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/odclearning-9bd7a.appspot.com/o/ODLLOGO.png?alt=media&token=1956b05e-3f2d-4885-9a11-df035976b31b\" alt=\"\">" +
                "  <div style=\"border-bottom: 1px grey solid\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p>Votre mot de passe pour Orange Digital Learning est le suivant:</p>\n" +
                "<h2 style=\"color: #2e6c80;\">"+password+"</h2>\n" +
                "<br>" +
                "<br>" +
                "<h2 style=\"color: #2e6c80;\"> <i>NB: Veuillez le modifier après la première connexion</i> </h2>\n" +
                "</div>";
    }

    public String mailTemplateWelcome(String fullName, String email) {
        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 20px; text-align: center\">\n" +
                "  <h1 style=\"color: #5e9ca0;'\">BIENVENUE SUR Orange Digital Learning</h1>\n" +
                " <img style=\"height: 100px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/odclearning-9bd7a.appspot.com/o/ODLLOGO.png?alt=media&token=1956b05e-3f2d-4885-9a11-df035976b31b\" alt=\"\">" +
                "  <div style=\"border-bottom: 1px grey solid\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p>Bonjour <strong>" + fullName + "</strong>,</p>\n" +
                "<p>Nous sommes ravis de vous accueillir sur Orange Digital Learning !</p>\n" +
                "<p>Votre compte a été créé avec succès avec l'adresse email: <strong>" + email + "</strong></p>\n" +
                "<p>Vous pouvez maintenant accéder à la plateforme et commencer votre apprentissage.</p>\n" +
                "<br>" +
                "<p style=\"color: #2e6c80;\">Bonne continuation dans votre parcours d'apprentissage !</p>\n" +
                "</div>";
    }

    public String mailTemplateInstructorCreated(String fullName, String email, String password, String siteUrl) {
        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 20px; text-align: center\">\n" +
                "  <h1 style=\"color: #5e9ca0;'\">COMPTE FORMATEUR CRÉÉ - Orange Digital Learning</h1>\n" +
                " <img style=\"height: 100px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/odclearning-9bd7a.appspot.com/o/ODLLOGO.png?alt=media&token=1956b05e-3f2d-4885-9a11-df035976b31b\" alt=\"\">" +
                "  <div style=\"border-bottom: 1px grey solid\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p>Bonjour <strong>" + fullName + "</strong>,</p>\n" +
                "<p>Votre compte formateur a été créé avec succès sur Orange Digital Learning !</p>\n" +
                "<p><strong>Vos identifiants de connexion:</strong></p>\n" +
                "<p>Email: <strong>" + email + "</strong></p>\n" +
                "<p>Mot de passe: <strong>" + password + "</strong></p>\n" +
                "<br>" +
                "<p><strong>Lien de connexion:</strong></p>\n" +
                "<p><a href=\"" + siteUrl + "\" style=\"color: #2e6c80; text-decoration: underline;\">" + siteUrl + "</a></p>\n" +
                "<br>" +
                "<p style=\"color: #2e6c80;\"><i>NB: Veuillez modifier votre mot de passe après la première connexion pour des raisons de sécurité.</i></p>\n" +
                "<br>" +
                "<p>Bienvenue dans l'équipe des formateurs !</p>\n" +
                "</div>";
    }

    public String mailTemplateAdminCreated(String fullName, String email, String siteUrl) {
        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 20px; text-align: center\">\n" +
                "  <h1 style=\"color: #5e9ca0;'\">COMPTE ADMINISTRATEUR CRÉÉ - Orange Digital Learning</h1>\n" +
                " <img style=\"height: 100px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/odclearning-9bd7a.appspot.com/o/ODLLOGO.png?alt=media&token=1956b05e-3f2d-4885-9a11-df035976b31b\" alt=\"\">" +
                "  <div style=\"border-bottom: 1px grey solid\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p>Bonjour <strong>" + fullName + "</strong>,</p>\n" +
                "<p>Votre compte administrateur a été créé avec succès sur Orange Digital Learning !</p>\n" +
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
                "  <h1 style=\"color: #FF6600; margin-bottom: 20px;\">Bienvenue sur Orange Digital Learning</h1>\n" +
                " <img style=\"height: 100px; margin-bottom: 20px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/odclearning-9bd7a.appspot.com/o/ODLLOGO.png?alt=media&token=1956b05e-3f2d-4885-9a11-df035976b31b\" alt=\"Orange Digital Learning Logo\">" +
                "  <div style=\"border-bottom: 1px grey solid; margin: 20px 0;\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p style=\"font-size: 16px; margin: 20px 0;\">Bonjour <strong>" + fullName + "</strong>,</p>\n" +
                "<p style=\"font-size: 14px; margin: 15px 0;\">Nous sommes ravis de vous accueillir sur Orange Digital Learning !</p>\n" +
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
                "<p style=\"font-size: 12px; color: #999; margin-top: 30px;\">L'équipe Orange Digital Learning</p>\n" +
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
//                "  <h1 style=\"color: #5e9ca0;'\">CONNEXION À Orange Digital Learning</h1>\n" +
//                " <img style=\"height: 100px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/logis-admin.appspot.com/o/icon.png?alt=media&token=597c6a6b-29a9-466a-9d69-fe9dbd52fe52\" alt=\"\">" +
//                "  <div style=\"border-bottom: 1px grey solid\">\n" +
//                "    \n" +
//                "  </div>\n" +
//                "\n" +
//                "<p>Votre code de validation pour Orange Digital Learning est le suivant:</p>\n" +
//                "<h2 style=\"color: #2e6c80;\">"+confirmationCode+"</h2>\n" +
//                "<p>Veuillez le modifier après la première connexion.</p>\n" +
//                "</div>";
//    }
//}
