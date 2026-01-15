package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.service.QuizService;
import com.odc.aws_learning.app.wrapper.QuizDTO;
import com.odc.aws_learning.app.wrapper.QuizSubmissionDTO;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {
    
    private final QuizService quizService;
    private final UserRepository userRepository;
    
    /**
     * Créer un quiz complet avec questions/réponses (ADMIN et INSTRUCTOR)
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<QuizDTO> createQuiz(@RequestBody QuizDTO quizDTO) {
        try {
            System.out.println("=== RECEPTION DU PAYLOAD QUIZ ===");
            System.out.println("Title: " + quizDTO.getTitle());
            System.out.println("Description: " + quizDTO.getDescription());
            System.out.println("CourseId: " + quizDTO.getCourseId());
            System.out.println("DurationMinutes: " + quizDTO.getDurationMinutes());
            System.out.println("ScoreMinimum: " + quizDTO.getScoreMinimum());
            System.out.println("Nombre de questions: " + (quizDTO.getQuestions() != null ? quizDTO.getQuestions().size() : 0));
            
            if (quizDTO.getQuestions() != null) {
                for (int i = 0; i < quizDTO.getQuestions().size(); i++) {
                    QuizDTO.QuestionDTO q = quizDTO.getQuestions().get(i);
                    System.out.println("Question " + i + ":");
                    System.out.println("  Content: " + q.getContent());
                    System.out.println("  Type: " + q.getType());
                    System.out.println("  Points: " + q.getPoints());
                    System.out.println("  Nombre de réponses: " + (q.getReponses() != null ? q.getReponses().size() : 0));
                    
                    if (q.getReponses() != null) {
                        for (int j = 0; j < q.getReponses().size(); j++) {
                            QuizDTO.ReponseDTO r = q.getReponses().get(j);
                            System.out.println("    Réponse " + j + ": " + r.getText() + " (Correct: " + r.getIsCorrect() + ")");
                        }
                    }
                }
            }
            
            CResponse<QuizDTO> response = quizService.createQuiz(quizDTO);
            System.out.println("Réponse du service: " + (response != null ? response.getMessage() : "NULL"));
            return response;
        } catch (Exception e) {
            System.err.println("ERREUR dans createQuiz: " + e.getMessage());
            e.printStackTrace();
            return CResponse.error("Erreur lors de la création du quiz: " + e.getMessage());
        }
    }
    
    /**
     * Récupérer tous les quiz d'un cours (USER/ADMIN/LEARNER/INSTRUCTOR)
     */
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public CResponse<List<QuizDTO>> getQuizzesByCourse(@PathVariable Long courseId) {
        return quizService.getQuizzesByCourse(courseId);
    }

    @GetMapping("/{quizId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public CResponse<QuizDTO> getQuizById(@PathVariable Long quizId) {
        return quizService.getQuizById(quizId);
    }
    
    /**
     * Mettre à jour un quiz complet avec questions/réponses (ADMIN et INSTRUCTOR)
     */
    @PutMapping("/update/{quizId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<QuizDTO> updateQuiz(@PathVariable Long quizId, @RequestBody QuizDTO quizDTO) {
        return quizService.updateQuiz(quizId, quizDTO);
    }
    
    /**
     * Supprimer un quiz (ADMIN et INSTRUCTOR)
     */
    @DeleteMapping("/delete/{quizId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> deleteQuiz(@PathVariable Long quizId) {
        return quizService.deleteQuiz(quizId);
    }
    
    /**
     * Soumettre ses réponses et recevoir son score (USER/ADMIN/LEARNER)
     */
    @PostMapping("/submit")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT')")
    public CResponse<?> submitQuiz(@RequestBody QuizSubmissionDTO submission) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return CResponse.error("Utilisateur non authentifié");
        }
        return quizService.submitQuiz(submission.getQuizId(), submission, currentUser);
    }
    
    /**
     * Récupère l'utilisateur actuellement authentifié
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> userOptional = userRepository.findByEmail(userDetails.getUsername());
            return userOptional.orElse(null);
        }
        return null;
    }
}
