package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Quiz;
import com.odc.aws_learning.app.entity.Certificate;
import com.odc.aws_learning.app.entity.UserQuizAttempt;
import com.odc.aws_learning.app.repository.QuizRepository;
import com.odc.aws_learning.app.repository.UserQuizAttemptRepository;
import com.odc.aws_learning.app.repository.CertificateRepository;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.service.CertificateService;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;
    private final QuizRepository quizRepository;
    private final UserQuizAttemptRepository userQuizAttemptRepository;
    private final CertificateRepository certificateRepository;
    private final CoursesRepository coursesRepository;
    private final UserRepository userRepository;

    @GetMapping("/download/{quizId}")
    @PreAuthorize("hasAnyRole('USER', 'APPRENANT', 'ADMIN')")
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

    @GetMapping("/instructor/{instructorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<CResponse<?>> getCertificatesByInstructor(@PathVariable Long instructorId) {
        // Récupérer tous les cours de l'instructeur
        List<Courses> instructorCourses = coursesRepository.findByInstructor_Id(instructorId);
        List<Long> courseIds = instructorCourses.stream().map(Courses::getId).collect(Collectors.toList());
        
        if (courseIds.isEmpty()) {
            return ResponseEntity.ok(CResponse.success(List.of(), "Aucun certificat trouvé pour cet instructeur"));
        }
        
        // Récupérer tous les certificats pour ces cours
        List<Certificate> certificates = certificateRepository.findAll().stream()
                .filter(cert -> courseIds.contains(cert.getCourse().getId()))
                .collect(Collectors.toList());
        
        // Mapper vers un DTO simple
        List<Map<String, Object>> certificateDtos = certificates.stream().map(cert -> {
            Map<String, Object> dto = new HashMap<>();
            dto.put("id", cert.getId());
            dto.put("uniqueCode", cert.getUniqueCode());
            dto.put("studentName", cert.getUser().getFullName());
            dto.put("studentEmail", cert.getUser().getEmail());
            dto.put("course", cert.getCourse().getTitle());
            dto.put("courseId", cert.getCourse().getId());
            dto.put("issuedDate", cert.getIssuedAt() != null ? cert.getIssuedAt().toString() : "");
            // Valide pendant 1 an par défaut
            Instant validUntil = cert.getIssuedAt() != null ? cert.getIssuedAt().plus(365, ChronoUnit.DAYS) : null;
            dto.put("validUntil", validUntil != null ? validUntil.toString() : "");
            dto.put("status", validUntil != null && validUntil.isAfter(Instant.now()) ? "Valide" : "Expiré");
            dto.put("certificateUrl", cert.getCertificateUrl());
            dto.put("avatar", cert.getUser().getAvatar());
            return dto;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(CResponse.success(certificateDtos, "Certificats récupérés avec succès"));
    }

    /** Liste de tous les apprenants certifiés (admin uniquement). Pagination : ?page=0&size=50 */
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CResponse<?>> getAllCertificates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        int effectiveSize = Math.min(Math.max(size, 1), 200);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
                Math.max(page, 0), effectiveSize,
                org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "issuedAt"));
        org.springframework.data.domain.Page<Certificate> certPage = certificateRepository.findAll(pageable);

        List<Map<String, Object>> dtos = certPage.getContent().stream().map(this::mapCertificateToDto).collect(Collectors.toList());

        Map<String, Object> payload = new HashMap<>();
        payload.put("content", dtos);
        payload.put("totalElements", certPage.getTotalElements());
        payload.put("totalPages", certPage.getTotalPages());
        payload.put("page", certPage.getNumber());
        payload.put("size", certPage.getSize());

        return ResponseEntity.ok(CResponse.success(payload, "Liste des attestations (certificats)"));
    }

    private Map<String, Object> mapCertificateToDto(Certificate cert) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", cert.getId());
        dto.put("uniqueCode", cert.getUniqueCode());
        dto.put("studentName", cert.getUser().getFullName());
        dto.put("studentEmail", cert.getUser().getEmail());
        dto.put("course", cert.getCourse().getTitle());
        dto.put("courseId", cert.getCourse().getId());
        dto.put("issuedDate", cert.getIssuedAt() != null ? cert.getIssuedAt().toString() : "");
        Instant validUntil = cert.getIssuedAt() != null ? cert.getIssuedAt().plus(365, ChronoUnit.DAYS) : null;
        dto.put("validUntil", validUntil != null ? validUntil.toString() : "");
        dto.put("status", validUntil != null && validUntil.isAfter(Instant.now()) ? "Valide" : "Expiré");
        dto.put("certificateUrl", cert.getCertificateUrl());
        dto.put("avatar", cert.getUser().getAvatar());
        return dto;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return null;
        String email = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            email = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else if (authentication.getPrincipal() instanceof String && !"anonymousUser".equals(authentication.getPrincipal())) {
            email = (String) authentication.getPrincipal();
        }
        if (email != null && !email.isEmpty()) {
            return userRepository.findByEmailWithInstructor(email).orElse(null);
        }
        return null;
    }
}
