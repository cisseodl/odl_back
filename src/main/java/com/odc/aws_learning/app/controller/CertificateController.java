package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.entity.Quiz;
import com.odc.aws_learning.app.entity.UserQuizAttempt;
import com.odc.aws_learning.app.repository.QuizRepository;
import com.odc.aws_learning.app.repository.UserQuizAttemptRepository;
import com.odc.aws_learning.app.service.CertificateService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;
    private final QuizRepository quizRepository;
    private final UserQuizAttemptRepository userQuizAttemptRepository;

    @GetMapping("/download/{quizId}")
    @PreAuthorize("hasAnyRole('USER', 'LEARNER', 'ADMIN')")
    public ResponseEntity<?> downloadCertificate(@PathVariable Long quizId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(CResponse.error("Utilisateur non authentifié"), HttpStatus.UNAUTHORIZED);
        }

        Optional<Quiz> quizOptional = quizRepository.findById(quizId);
        if (quizOptional.isEmpty()) {
            return new ResponseEntity<>(CResponse.error("Quiz introuvable"), HttpStatus.NOT_FOUND);
        }
        Quiz quiz = quizOptional.get();

        // Meilleure tentative de l'utilisateur pour ce quiz (score le plus élevé)
        Optional<UserQuizAttempt> bestAttemptOpt = userQuizAttemptRepository
                .findFirstByUserIdAndQuizIdOrderByScoreDesc(currentUser.getId(), quizId);

        if (bestAttemptOpt.isEmpty()) {
            return new ResponseEntity<>(CResponse.error("Aucune tentative trouvée pour ce quiz"), HttpStatus.FORBIDDEN);
        }

        UserQuizAttempt attempt = bestAttemptOpt.get();

        // Vérifier si le score atteint le minimum requis
        Integer scoreMinimum = quiz.getScoreMinimum();
        double score = attempt.getScore() != null ? attempt.getScore() : 0.0;
        if (scoreMinimum != null && score < scoreMinimum) {
            return new ResponseEntity<>(
                    CResponse.error("Score insuffisant pour obtenir le certificat"),
                    HttpStatus.FORBIDDEN
            );
        }

        byte[] pdfBytes = certificateService.generateCertificate(currentUser, quiz, attempt);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificat-" + quizId + ".pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }
}
