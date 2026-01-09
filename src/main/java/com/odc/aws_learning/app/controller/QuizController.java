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
     * Créer un quiz complet avec questions/réponses (ADMIN only)
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<QuizDTO> createQuiz(@RequestBody QuizDTO quizDTO) {
        return quizService.createQuiz(quizDTO);
    }
    
    /**
     * Récupérer tous les quiz d'un cours (USER/ADMIN/LEARNER)
     */
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT')")
    public CResponse<List<QuizDTO>> getQuizzesByCourse(@PathVariable Long courseId) {
        return quizService.getQuizzesByCourse(courseId);
    }

    @GetMapping("/{quizId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT')")
    public CResponse<QuizDTO> getQuizById(@PathVariable Long quizId) {
        return quizService.getQuizById(quizId);
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
