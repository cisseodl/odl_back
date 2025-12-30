package com.odc.aws_learning.app.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.odc.aws_learning.app.entity.Quiz;
import com.odc.aws_learning.app.entity.UserQuizAttempt;
import com.odc.aws_learning.auth.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CertificateService {

    public byte[] generateCertificate(User user, Quiz quiz, UserQuizAttempt attempt) {
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

            String quizTitle = quiz.getTitre() != null ? quiz.getTitre() : "Module de formation";
            Paragraph forQuiz = new Paragraph(
                    "Pour avoir réussi le module : " + quizTitle,
                    textFont
            );
            forQuiz.setAlignment(Element.ALIGN_CENTER);
            forQuiz.setSpacingAfter(15f);
            document.add(forQuiz);

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

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du certificat PDF", e);
        }
    }
}
