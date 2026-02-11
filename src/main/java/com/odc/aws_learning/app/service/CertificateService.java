package com.odc.aws_learning.app.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.odc.aws_learning.app.entity.Quiz;
import com.odc.aws_learning.app.entity.UserQuizAttempt;
import com.odc.aws_learning.app.entity.Certificate;
import com.odc.aws_learning.app.entity.Courses;
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
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final UploadFileService uploadFileService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${app.server.base-url:https://api.smart-odc.com}")
    private String serverBaseUrl;

    public CertificateService(CertificateRepository certificateRepository, UploadFileService uploadFileService) {
        this.certificateRepository = certificateRepository;
        this.uploadFileService = uploadFileService;
    }
    public CResponse<Certificate> generateCertificate(User user, Courses course, Quiz quiz, UserQuizAttempt attempt) {
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            // Polices
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.DARK_GRAY);
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.DARK_GRAY);

            // Titre centré
            Paragraph title = new Paragraph("CERTIFICAT DE RÉUSSITE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(30f);
            document.add(title);

            // Ligne de séparation
            PdfPTable separatorTable = new PdfPTable(1);
            separatorTable.setWidthPercentage(100);
            PdfPCell separatorCell = new PdfPCell();
            separatorCell.setBorderWidthBottom(2f);
            separatorCell.setBorder(Rectangle.BOTTOM);
            separatorCell.setFixedHeight(10f);
            separatorCell.setBorderColor(BaseColor.LIGHT_GRAY);
            separatorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            separatorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            separatorCell.setPhrase(new Phrase(""));
            separatorTable.addCell(separatorCell);
            separatorTable.setSpacingAfter(30f);
            document.add(separatorTable);

            // Contenu principal
            String fullName = user.getFullName() != null ? user.getFullName() : user.getEmail();
            Paragraph awardedTo = new Paragraph("Décerné à ", textFont);
            awardedTo.setAlignment(Element.ALIGN_CENTER);
            document.add(awardedTo);

            Paragraph name = new Paragraph(fullName, subtitleFont);
            name.setAlignment(Element.ALIGN_CENTER);
            name.setSpacingAfter(20f);
            document.add(name);

            String courseTitle = course.getTitle() != null ? course.getTitle() : "Cours de formation"; // Utiliser le titre du cours
            Paragraph forCourse = new Paragraph(
                    "Pour avoir réussi le cours : " + courseTitle,
                    textFont
            );
            forCourse.setAlignment(Element.ALIGN_CENTER);
            forCourse.setSpacingAfter(15f);
            document.add(forCourse);

            int scoreObtenu = attempt.getScore() != null ? attempt.getScore().intValue() : 0;
            int scoreTotal = attempt.getScoreTotal() != null ? attempt.getScoreTotal() : 0;
            Paragraph score = new Paragraph(
                    "Score : " + scoreObtenu + " / " + scoreTotal,
                    textFont
            );
            score.setAlignment(Element.ALIGN_CENTER);
            score.setSpacingAfter(20f);
            document.add(score);

            LocalDate today = LocalDate.now();
            String dateStr = today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph date = new Paragraph("Date : " + dateStr, textFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(40f);
            document.add(date);

            // Zone de signature
            Paragraph signature = new Paragraph("Signature de l'administration", textFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            document.close();

            byte[] pdfBytes = out.toByteArray();

            // Générer un nom de fichier unique pour le certificat
            String certificateFileName = "certificate_" + UUID.randomUUID().toString() + ".pdf";

            // Uploader le PDF vers le stockage local (Elastic Beanstalk)
            try {
                String localFolderPath = uploadDir + "/certificates";
                ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);
                String savedFileName = uploadFileService.uploadInputStream(bis, localFolderPath, certificateFileName, pdfBytes.length, "application/pdf");
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
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            // Polices
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.DARK_GRAY);
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.DARK_GRAY);

            // Titre centré
            Paragraph title = new Paragraph("CERTIFICAT DE RÉUSSITE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(30f);
            document.add(title);

            // Ligne de séparation
            PdfPTable separatorTable = new PdfPTable(1);
            separatorTable.setWidthPercentage(100);
            PdfPCell separatorCell = new PdfPCell();
            separatorCell.setBorderWidthBottom(2f);
            separatorCell.setBorder(Rectangle.BOTTOM);
            separatorCell.setFixedHeight(10f);
            separatorCell.setBorderColor(BaseColor.LIGHT_GRAY);
            separatorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            separatorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            separatorCell.setPhrase(new Phrase(""));
            separatorTable.addCell(separatorCell);
            separatorTable.setSpacingAfter(30f);
            document.add(separatorTable);

            // Contenu principal
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
                    "Pour avoir réussi le cours : " + courseTitle,
                    textFont
            );
            forCourse.setAlignment(Element.ALIGN_CENTER);
            forCourse.setSpacingAfter(15f);
            document.add(forCourse);

            Paragraph score = new Paragraph(
                    "Score à l'évaluation : " + String.format("%.1f", evaluationScore) + " / 100",
                    textFont
            );
            score.setAlignment(Element.ALIGN_CENTER);
            score.setSpacingAfter(20f);
            document.add(score);

            LocalDate today = LocalDate.now();
            String dateStr = today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph date = new Paragraph("Date : " + dateStr, textFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(40f);
            document.add(date);

            // Zone de signature
            Paragraph signature = new Paragraph("Signature de l'administration", textFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            document.close();

            byte[] pdfBytes = out.toByteArray();

            // Générer un nom de fichier unique pour le certificat
            String certificateFileName = "certificate_" + UUID.randomUUID().toString() + ".pdf";

            // Uploader le PDF vers le stockage local (Elastic Beanstalk)
            try {
                String localFolderPath = uploadDir + "/certificates";
                ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);
                String savedFileName = uploadFileService.uploadInputStream(bis, localFolderPath, certificateFileName, pdfBytes.length, "application/pdf");
                String certificateUrl = serverBaseUrl + "/awsodclearning/api/files/certificates/" + savedFileName;
                
                // Enregistrer l'entité Certificate dans la base de données
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
     * Génère un certificat pour validation des labs par l'instructeur (mode certification BY_LABS).
     * L'apprenant a réalisé les labs du cours ; l'instructeur valide et déclenche l'attribution du certificat.
     */
    public CResponse<Certificate> generateCertificateForLabsCompletion(User user, Courses course) {
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.DARK_GRAY);
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.DARK_GRAY);

            Paragraph title = new Paragraph("CERTIFICAT DE RÉUSSITE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(30f);
            document.add(title);

            PdfPTable separatorTable = new PdfPTable(1);
            separatorTable.setWidthPercentage(100);
            PdfPCell separatorCell = new PdfPCell();
            separatorCell.setBorderWidthBottom(2f);
            separatorCell.setBorder(Rectangle.BOTTOM);
            separatorCell.setFixedHeight(10f);
            separatorCell.setBorderColor(BaseColor.LIGHT_GRAY);
            separatorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            separatorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            separatorCell.setPhrase(new Phrase(""));
            separatorTable.addCell(separatorCell);
            separatorTable.setSpacingAfter(30f);
            document.add(separatorTable);

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

            Paragraph signature = new Paragraph("Signature de l'administration", textFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            document.close();

            byte[] pdfBytes = out.toByteArray();
            String certificateFileName = "certificate_" + UUID.randomUUID().toString() + ".pdf";

            try {
                String localFolderPath = uploadDir + "/certificates";
                ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);
                String savedFileName = uploadFileService.uploadInputStream(bis, localFolderPath, certificateFileName, pdfBytes.length, "application/pdf");
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
