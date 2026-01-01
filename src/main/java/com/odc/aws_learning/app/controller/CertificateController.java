package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Quiz;
import com.odc.aws_learning.app.entity.Certificate;
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
@RequestMapping("/api/certificates")
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

        Optional<UserQuizAttempt> bestAttemptOpt = userQuizAttemptRepository
                .findFirstByUserIdAndQuizIdOrderByScoreDesc(currentUser.getId(), quizId);
        UserQuizAttempt attempt = bestAttemptOpt.get();
        Courses course = quiz.getCourse(); // Récupérer le cours
        // ...
        CResponse<Certificate> response = certificateService.generateCertificate(currentUser, course, quiz, attempt);

        if (response.isSuccess()) {
            Certificate generatedCertificate = response.getData();
            if (generatedCertificate != null && generatedCertificate.getCertificateUrl() != null) {
                // Au lieu de retourner le PDF directement, rediriger vers l'URL S3
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.LOCATION, generatedCertificate.getCertificateUrl());
                return new ResponseEntity<>(headers, HttpStatus.FOUND); // HTTP 302 Found pour redirection
            } else {
                return new ResponseEntity<>(CResponse.error("Erreur lors de la génération du certificat: URL non trouvée."), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(CResponse.error(response.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{uniqueCode}")
    public ResponseEntity<CResponse<?>> verifyCertificate(@PathVariable String uniqueCode) {
        CResponse<?> response = certificateService.getCertificateByUniqueCode(uniqueCode);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }
}
