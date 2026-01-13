package com.odc.aws_learning.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class SendEmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailWithAttachment(String email, String message, String subject) {
        try {
            System.out.println("start sending...");
            MimeMessage msg = javaMailSender.createMimeMessage();

            // true = multipart message
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
//            helper.setTo(applicationUser.getUsername());
            helper.setTo(new String[]{email});
            helper.setFrom("cisseodl@gmail.com"); // Définir l'expéditeur

            helper.setSubject(subject);

            // default = text/plain
            //helper.setText("Check attachment for image!");

            helper.setText(message, true);
            javaMailSender.send(msg);
            System.out.println("end sending...");

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
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
        return "<div style=\"border: 1px grey solid; border-radius: 10px; padding: 20px; text-align: center\">\n" +
                "  <h1 style=\"color: #5e9ca0;'\">COMPTE APPRENANT CRÉÉ - Orange Digital Learning</h1>\n" +
                " <img style=\"height: 100px;\" class=\"welcomeImg\" src=\"https://firebasestorage.googleapis.com/v0/b/odclearning-9bd7a.appspot.com/o/ODLLOGO.png?alt=media&token=1956b05e-3f2d-4885-9a11-df035976b31b\" alt=\"\">" +
                "  <div style=\"border-bottom: 1px grey solid\">\n" +
                "    \n" +
                "  </div>\n" +
                "\n" +
                "<p>Bonjour <strong>" + fullName + "</strong>,</p>\n" +
                "<p>Votre compte apprenant a été créé avec succès sur Orange Digital Learning !</p>\n" +
                "<p><strong>Vos identifiants de connexion:</strong></p>\n" +
                "<p>Email: <strong>" + email + "</strong></p>\n" +
                "<br>" +
                "<p><strong>Lien de connexion:</strong></p>\n" +
                "<p><a href=\"" + siteUrl + "\" style=\"color: #2e6c80; text-decoration: underline;\">" + siteUrl + "</a></p>\n" +
                "<br>" +
                "<p>Bienvenue dans votre parcours d'apprentissage !</p>\n" +
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
