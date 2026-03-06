package com.odc.aws_learning.app.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.InputStream;
import com.odc.aws_learning.app.entity.Quiz;
import com.odc.aws_learning.app.entity.UserQuizAttempt;
import com.odc.aws_learning.app.entity.Certificate;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.EvaluationAttempt;
import com.odc.aws_learning.app.service.UploadFileService;
import com.odc.aws_learning.auth.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.odc.aws_learning.app.repository.CertificateRepository;
import java.util.Optional;
import com.odc.aws_learning.auth.base.response.CResponse;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final UploadFileService uploadFileService;
    private final SendEmailService sendEmailService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${app.server.base-url:https://api.smart-odc.com}")
    private String serverBaseUrl;

    /** Charte ODL : orange (#FF7900) en accent uniquement (titre, ligne, signature marque), texte en noir, fond blanc */
    private static final BaseColor ORANGE = new BaseColor(255, 121, 0);

    /**
     * Ajoute le logo ODL en haut du certificat s'il est présent dans classpath (static/logo.png ou logo.png).
     */
    private void addLogoIfPresent(Document document) {
        try {
            InputStream is = getClass().getResourceAsStream("/static/logo.png");
            if (is == null) is = getClass().getResourceAsStream("/logo.png");
            if (is != null) {
                byte[] bytes = is.readAllBytes();
                is.close();
                Image img = Image.getInstance(bytes);
                img.scaleToFit(140, 70);
                img.setAlignment(Element.ALIGN_CENTER);
                img.setSpacingAfter(15f);
                document.add(img);
            }
        } catch (Exception ignored) { /* logo optionnel */ }
    }

    /** Espace vertical pour centrer le contenu (mode paysage : hauteur page 595pt). */
    private void addVerticalSpacer(Document document, boolean landscape) {
        float space = landscape ? 95f : 60f;
        Paragraph spacer = new Paragraph(" ");
        spacer.setSpacingBefore(space);
        spacer.setSpacingAfter(0f);
        try { document.add(spacer); } catch (DocumentException ignored) { }
    }

    public CResponse<Certificate> generateCertificate(User user, Courses course, Quiz quiz, UserQuizAttempt attempt) {
        try {
            Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            addLogoIfPresent(document);
            addVerticalSpacer(document, true);

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, ORANGE);
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

            Paragraph title = new Paragraph("Certificat d'achèvement du cours", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(30f);
            document.add(title);

            PdfPTable separatorTable = new PdfPTable(1);
            separatorTable.setWidthPercentage(100);
            PdfPCell separatorCell = new PdfPCell();
            separatorCell.setBorderWidthBottom(2f);
            separatorCell.setBorder(Rectangle.BOTTOM);
            separatorCell.setFixedHeight(10f);
            separatorCell.setBorderColor(ORANGE);
            separatorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            separatorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            separatorCell.setPhrase(new Phrase(""));
            separatorTable.addCell(separatorCell);
            separatorTable.setSpacingAfter(30f);
            document.add(separatorTable);

            Font felicitationsFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
            Paragraph felicitations = new Paragraph("Félicitations !", felicitationsFont);
            felicitations.setAlignment(Element.ALIGN_CENTER);
            felicitations.setSpacingAfter(15f);
            document.add(felicitations);

            String fullName = user.getFullName() != null ? user.getFullName() : user.getEmail();
            Paragraph awardedTo = new Paragraph("Décerné à ", textFont);
            awardedTo.setAlignment(Element.ALIGN_CENTER);
            document.add(awardedTo);

            Paragraph name = new Paragraph(fullName, subtitleFont);
            name.setAlignment(Element.ALIGN_CENTER);
            name.setSpacingAfter(20f);
            document.add(name);

            String courseTitle = course.getTitle() != null ? course.getTitle() : "Cours de formation";
            Paragraph forCourse = new Paragraph("Pour avoir réussi le cours : " + courseTitle, textFont);
            forCourse.setAlignment(Element.ALIGN_CENTER);
            forCourse.setSpacingAfter(25f);
            document.add(forCourse);

            LocalDate today = LocalDate.now();
            String dateStr = today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph date = new Paragraph("Date : " + dateStr, textFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(40f);
            document.add(date);

            Font signatureFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, ORANGE);
            Paragraph signature = new Paragraph("Orange Digital Center", signatureFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            document.close();

            byte[] pdfBytes = out.toByteArray();

            // Générer un nom de fichier unique pour le certificat
            String certificateFileName = "certificate_" + UUID.randomUUID().toString() + ".pdf";

            // Uploader le PDF vers le stockage local (Elastic Beanstalk)
            try {
                String s3Folder = "certificates";
                ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);
                String savedFileName = uploadFileService.uploadInputStream(bis, s3Folder, certificateFileName, pdfBytes.length, "application/pdf");
                String certificateUrl = serverBaseUrl + "/awsodclearning/api/files/certificates/" + savedFileName;
                
                // Enregistrer l'entité Certificate dans la base de données
                Certificate certificate = new Certificate();
                certificate.setUniqueCode(UUID.randomUUID().toString()); // Générer un code unique pour le certificat
                certificate.setUser(user);
                certificate.setCourse(course);
                certificate.setIssuedAt(java.time.Instant.now());
                certificate.setCertificateUrl(certificateUrl);
                certificateRepository.save(certificate);

                return CResponse.success(certificate, "Certificat généré et enregistré avec succès.");
            } catch (IOException ioException) {
                throw new RuntimeException("Erreur lors de l'upload du certificat vers le stockage local: " + ioException.getMessage(), ioException);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du certificat PDF", e);
        }
    }

    public CResponse<?> getCertificateByUniqueCode(String uniqueCode) {
        Optional<com.odc.aws_learning.app.entity.Certificate> certificateOptional = certificateRepository.findByUniqueCode(uniqueCode);
        if (certificateOptional.isEmpty()) {
            return CResponse.error("Certificat non trouvé avec le code unique : " + uniqueCode);
        }
        // Pour des raisons de sécurité et de performance, ne pas retourner le PDF directement
        // Retourner les informations du certificat (user, course, issuedAt, certificateUrl)
        return CResponse.success(certificateOptional.get(), "Certificat récupéré avec succès");
    }
    
    /**
     * Génère un certificat pour une évaluation réussie (score >= 70%)
     * @param user L'apprenant
     * @param course Le cours
     * @param evaluationScore Le score obtenu (0-100)
     * @return Le certificat généré
     */
    public CResponse<Certificate> generateCertificateForEvaluation(User user, Courses course, Double evaluationScore) {
        try {
            Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            addLogoIfPresent(document);
            addVerticalSpacer(document, true);
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, ORANGE);
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

            Paragraph title = new Paragraph("Certificat d'achèvement du cours", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(30f);
            document.add(title);

            PdfPTable separatorTable = new PdfPTable(1);
            separatorTable.setWidthPercentage(100);
            PdfPCell separatorCell = new PdfPCell();
            separatorCell.setBorderWidthBottom(2f);
            separatorCell.setBorder(Rectangle.BOTTOM);
            separatorCell.setFixedHeight(10f);
            separatorCell.setBorderColor(ORANGE);
            separatorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            separatorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            separatorCell.setPhrase(new Phrase(""));
            separatorTable.addCell(separatorCell);
            separatorTable.setSpacingAfter(30f);
            document.add(separatorTable);

            Font felicitationsFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
            Paragraph felicitations = new Paragraph("Félicitations !", felicitationsFont);
            felicitations.setAlignment(Element.ALIGN_CENTER);
            felicitations.setSpacingAfter(15f);
            document.add(felicitations);

            String fullName = user.getFullName() != null ? user.getFullName() : user.getEmail();
            Paragraph awardedTo = new Paragraph("Décerné à ", textFont);
            awardedTo.setAlignment(Element.ALIGN_CENTER);
            document.add(awardedTo);

            Paragraph name = new Paragraph(fullName, subtitleFont);
            name.setAlignment(Element.ALIGN_CENTER);
            name.setSpacingAfter(20f);
            document.add(name);

            String courseTitle = course.getTitle() != null ? course.getTitle() : "Cours de formation";
            Paragraph forCourse = new Paragraph("Pour avoir réussi le cours : " + courseTitle, textFont);
            forCourse.setAlignment(Element.ALIGN_CENTER);
            forCourse.setSpacingAfter(25f);
            document.add(forCourse);

            LocalDate today = LocalDate.now();
            String dateStr = today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph date = new Paragraph("Date : " + dateStr, textFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(40f);
            document.add(date);

            Font signatureFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, ORANGE);
            Paragraph signature = new Paragraph("Orange Digital Center", signatureFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            document.close();

            byte[] pdfBytes = out.toByteArray();

            String certificateFileName = "certificate_" + UUID.randomUUID().toString() + ".pdf";
            try {
                String s3Folder = "certificates";
                ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);
                String savedFileName = uploadFileService.uploadInputStream(bis, s3Folder, certificateFileName, pdfBytes.length, "application/pdf");
                String certificateUrl = serverBaseUrl + "/awsodclearning/api/files/certificates/" + savedFileName;
                Certificate certificate = new Certificate();
                certificate.setUniqueCode(UUID.randomUUID().toString());
                certificate.setUser(user);
                certificate.setCourse(course);
                certificate.setIssuedAt(java.time.Instant.now());
                certificate.setCertificateUrl(certificateUrl);
                certificateRepository.save(certificate);
                return CResponse.success(certificate, "Certificat généré et enregistré avec succès.");
            } catch (IOException ioException) {
                throw new RuntimeException("Erreur lors de l'upload du certificat vers le stockage local: " + ioException.getMessage(), ioException);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du certificat PDF", e);
        }
    }

    /**
     * Génère un certificat pour une évaluation réussie (score >= 70%) en utilisant le nom et l'email
     * saisis par l'apprenant avant l'examen. Envoie un email avec le lien du certificat si certificateEmail est renseigné.
     */
    public CResponse<Certificate> generateCertificateForEvaluation(EvaluationAttempt attempt) {
        User user = attempt.getUser();
        Courses course = attempt.getEvaluation().getCourse();
        Double evaluationScore = attempt.getScore();
        String displayName = (attempt.getCertificateDisplayName() != null && !attempt.getCertificateDisplayName().isBlank())
            ? attempt.getCertificateDisplayName().trim()
            : (user.getFullName() != null ? user.getFullName() : user.getEmail());
        try {
            Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();
            addLogoIfPresent(document);
            addVerticalSpacer(document, true);
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, ORANGE);
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            Paragraph title = new Paragraph("Certificat d'achèvement du cours", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(30f);
            document.add(title);
            PdfPTable separatorTable = new PdfPTable(1);
            separatorTable.setWidthPercentage(100);
            PdfPCell separatorCell = new PdfPCell();
            separatorCell.setBorderWidthBottom(2f);
            separatorCell.setBorder(Rectangle.BOTTOM);
            separatorCell.setFixedHeight(10f);
            separatorCell.setBorderColor(ORANGE);
            separatorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            separatorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            separatorCell.setPhrase(new Phrase(""));
            separatorTable.addCell(separatorCell);
            separatorTable.setSpacingAfter(30f);
            document.add(separatorTable);
            Font felicitationsFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
            Paragraph felicitations = new Paragraph("Félicitations !", felicitationsFont);
            felicitations.setAlignment(Element.ALIGN_CENTER);
            felicitations.setSpacingAfter(15f);
            document.add(felicitations);
            Paragraph awardedTo = new Paragraph("Décerné à ", textFont);
            awardedTo.setAlignment(Element.ALIGN_CENTER);
            document.add(awardedTo);
            Paragraph name = new Paragraph(displayName, subtitleFont);
            name.setAlignment(Element.ALIGN_CENTER);
            name.setSpacingAfter(20f);
            document.add(name);
            String courseTitle = course.getTitle() != null ? course.getTitle() : "Cours de formation";
            Paragraph forCourse = new Paragraph("Pour avoir réussi le cours : " + courseTitle, textFont);
            forCourse.setAlignment(Element.ALIGN_CENTER);
            forCourse.setSpacingAfter(25f);
            document.add(forCourse);
            LocalDate today = LocalDate.now();
            String dateStr = today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph date = new Paragraph("Date : " + dateStr, textFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(40f);
            document.add(date);
            Font signatureFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, ORANGE);
            Paragraph signature = new Paragraph("Orange Digital Center", signatureFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);
            document.close();
            byte[] pdfBytes = out.toByteArray();
            String certificateFileName = "certificate_" + UUID.randomUUID().toString() + ".pdf";
            String s3Folder = "certificates";
            ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);
            String savedFileName = uploadFileService.uploadInputStream(bis, s3Folder, certificateFileName, pdfBytes.length, "application/pdf");
            String certificateUrl = serverBaseUrl + "/awsodclearning/api/files/certificates/" + savedFileName;
            Certificate certificate = new Certificate();
            certificate.setUniqueCode(UUID.randomUUID().toString());
            certificate.setUser(user);
            certificate.setCourse(course);
            certificate.setIssuedAt(java.time.Instant.now());
            certificate.setCertificateUrl(certificateUrl);
            certificateRepository.save(certificate);
            String emailTo = (attempt.getCertificateEmail() != null && !attempt.getCertificateEmail().isBlank()) ? attempt.getCertificateEmail().trim() : null;
            if (emailTo != null && sendEmailService != null && sendEmailService.isEmailConfigured()) {
                try {
                    String message = "Félicitations " + displayName + " !\n\nVous avez réussi l'évaluation. Votre certificat est disponible :\n" + certificateUrl + "\n\nCordialement,\nL'équipe Orange Digital Center.";
                    sendEmailService.sendEmail(emailTo, message, "Votre certificat Orange Digital Center");
                } catch (Exception e) {
                    System.err.println("Erreur envoi email certificat: " + e.getMessage());
                }
            }
            return CResponse.success(certificate, "Certificat généré et enregistré avec succès.");
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du certificat PDF (tentative)", e);
        }
    }

    /**
     * Génère un certificat pour validation des labs par l'instructeur (mode certification BY_LABS).
     * L'apprenant a réalisé les labs du cours ; l'instructeur valide et déclenche l'attribution du certificat.
     */
    public CResponse<Certificate> generateCertificateForLabsCompletion(User user, Courses course) {
        try {
            Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            addLogoIfPresent(document);
            addVerticalSpacer(document, true);
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, ORANGE);
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

            Paragraph title = new Paragraph("Certificat d'achèvement du cours", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(30f);
            document.add(title);

            PdfPTable separatorTable = new PdfPTable(1);
            separatorTable.setWidthPercentage(100);
            PdfPCell separatorCell = new PdfPCell();
            separatorCell.setBorderWidthBottom(2f);
            separatorCell.setBorder(Rectangle.BOTTOM);
            separatorCell.setFixedHeight(10f);
            separatorCell.setBorderColor(ORANGE);
            separatorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            separatorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            separatorCell.setPhrase(new Phrase(""));
            separatorTable.addCell(separatorCell);
            separatorTable.setSpacingAfter(30f);
            document.add(separatorTable);

            Font felicitationsFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
            Paragraph felicitations = new Paragraph("Félicitations !", felicitationsFont);
            felicitations.setAlignment(Element.ALIGN_CENTER);
            felicitations.setSpacingAfter(15f);
            document.add(felicitations);

            String fullName = user.getFullName() != null ? user.getFullName() : user.getEmail();
            Paragraph awardedTo = new Paragraph("Décerné à ", textFont);
            awardedTo.setAlignment(Element.ALIGN_CENTER);
            document.add(awardedTo);

            Paragraph name = new Paragraph(fullName, subtitleFont);
            name.setAlignment(Element.ALIGN_CENTER);
            name.setSpacingAfter(20f);
            document.add(name);

            String courseTitle = course.getTitle() != null ? course.getTitle() : "Cours de formation";
            Paragraph forCourse = new Paragraph(
                    "Pour avoir complété avec succès tous les labs du cours : " + courseTitle,
                    textFont
            );
            forCourse.setAlignment(Element.ALIGN_CENTER);
            forCourse.setSpacingAfter(20f);
            document.add(forCourse);

            LocalDate today = LocalDate.now();
            String dateStr = today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph date = new Paragraph("Date : " + dateStr, textFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(40f);
            document.add(date);

            Font signatureFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, ORANGE);
            Paragraph signature = new Paragraph("Orange Digital Center", signatureFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            document.close();

            byte[] pdfBytes = out.toByteArray();
            String certificateFileName = "certificate_" + UUID.randomUUID().toString() + ".pdf";

            try {
                String s3Folder = "certificates";
                ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);
                String savedFileName = uploadFileService.uploadInputStream(bis, s3Folder, certificateFileName, pdfBytes.length, "application/pdf");
                String certificateUrl = serverBaseUrl + "/awsodclearning/api/files/certificates/" + savedFileName;

                Certificate certificate = new Certificate();
                certificate.setUniqueCode(UUID.randomUUID().toString());
                certificate.setUser(user);
                certificate.setCourse(course);
                certificate.setIssuedAt(java.time.Instant.now());
                certificate.setCertificateUrl(certificateUrl);
                certificateRepository.save(certificate);

                return CResponse.success(certificate, "Certificat généré et enregistré avec succès.");
            } catch (IOException ioException) {
                throw new RuntimeException("Erreur lors de l'upload du certificat: " + ioException.getMessage(), ioException);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du certificat PDF", e);
        }
    }
}
