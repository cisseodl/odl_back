package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.*;
import com.odc.aws_learning.app.repository.*;
import com.odc.aws_learning.app.wrapper.QuizDTO;
import com.odc.aws_learning.app.wrapper.QuizResultDTO;
import com.odc.aws_learning.app.wrapper.QuizSubmissionDTO;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {
    
    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizReponseRepository quizReponseRepository;
    private final UserQuizAttemptRepository userQuizAttemptRepository;
    private final CoursesRepository coursesRepository;
    
    /**
     * Crée un quiz complet avec ses questions et réponses
     */
    @Transactional
    public CResponse<QuizDTO> createQuiz(QuizDTO quizDTO) {
        try {
            Optional<Courses> courseOptional = coursesRepository.findById(quizDTO.getCourseId());
            if (courseOptional.isEmpty()) {
                return CResponse.error("Cours non trouvé avec l'ID: " + quizDTO.getCourseId());
            }
            
            Quiz quiz = new Quiz();
            quiz.setTitre(quizDTO.getTitre());
            quiz.setDescription(quizDTO.getDescription());
            quiz.setCourse(courseOptional.get());
            quiz.setDureeMinutes(quizDTO.getDureeMinutes());
            quiz.setScoreMinimum(quizDTO.getScoreMinimum());
            quiz.setActivate(true);
            
            Quiz savedQuiz = quizRepository.save(quiz);
            
            // Créer les questions et réponses
            if (quizDTO.getQuestions() != null) {
                for (QuizDTO.QuestionDTO questionDTO : quizDTO.getQuestions()) {
                    QuizQuestion question = new QuizQuestion();
                    question.setContenu(questionDTO.getContenu());
                    question.setType(questionDTO.getType());
                    question.setPoints(questionDTO.getPoints());
                    question.setQuiz(savedQuiz);
                    
                    QuizQuestion savedQuestion = quizQuestionRepository.save(question);
                    
                    // Créer les réponses
                    if (questionDTO.getReponses() != null) {
                        for (QuizDTO.ReponseDTO reponseDTO : questionDTO.getReponses()) {
                            QuizReponse reponse = new QuizReponse();
                            reponse.setTexte(reponseDTO.getTexte());
                            reponse.setEstCorrecte(reponseDTO.getEstCorrecte());
                            reponse.setQuestion(savedQuestion);
                            quizReponseRepository.save(reponse);
                        }
                    }
                }
            }
            
            return CResponse.success(quizDTO, "Quiz créé avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return CResponse.error("Erreur lors de la création du quiz: " + e.getMessage());
        }
    }
    
    /**
     * Récupère tous les quiz d'un cours
     */
    public CResponse<List<QuizDTO>> getQuizzesByCourse(Long courseId) {
        try {
            List<Quiz> quizzes = quizRepository.findByCourseIdAndActivateTrue(courseId);
            List<QuizDTO> quizDTOs = quizzes.stream().map(this::convertToDTO).collect(Collectors.toList());
            return CResponse.success(quizDTOs, "Quiz récupérés avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération des quiz: " + e.getMessage());
        }
    }

    public CResponse<QuizDTO> getQuizById(Long quizId) {
        try {
            Optional<Quiz> quizOptional = quizRepository.findById(quizId);
            if (quizOptional.isEmpty()) {
                return CResponse.error("Quiz non trouvé avec l'ID: " + quizId);
            }
            return CResponse.success(convertToDTO(quizOptional.get()), "Quiz récupéré avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération du quiz: " + e.getMessage());
        }
    }
    
    /**
     * Méthode critique : Calcule le score en comparant les réponses de l'étudiant avec les bonnes réponses
     */
    @Transactional
    public CResponse<QuizResultDTO> submitQuiz(Long quizId, QuizSubmissionDTO submission, User user) {
        try {
            Optional<Quiz> quizOptional = quizRepository.findById(quizId);
            if (quizOptional.isEmpty()) {
                return CResponse.error("Quiz non trouvé avec l'ID: " + quizId);
            }
            
            Quiz quiz = quizOptional.get();
            
            // Récupérer toutes les questions du quiz
            List<QuizQuestion> questions = quizQuestionRepository.findByQuizId(quizId);
            
            int scoreObtenu = 0;
            int scoreTotal = 0;
            List<QuizResultDTO.QuestionResultDTO> detailsQuestions = new ArrayList<>();
            
            // Calculer le score pour chaque question
            for (QuizQuestion question : questions) {
                scoreTotal += question.getPoints();
                
                // Trouver la réponse de l'utilisateur pour cette question
                QuizSubmissionDTO.AnswerDTO userAnswer = submission.getAnswers().stream()
                        .filter(a -> a.getQuestionId().equals(question.getId()))
                        .findFirst()
                        .orElse(null);
                
                // Récupérer les bonnes réponses pour cette question
                List<QuizReponse> bonnesReponses = quizReponseRepository
                        .findByQuestionIdAndEstCorrecteTrue(question.getId());
                List<Long> bonnesReponseIds = bonnesReponses.stream()
                        .map(QuizReponse::getId)
                        .collect(Collectors.toList());
                
                int pointsObtenus = 0;
                boolean correcte = false;
                List<Long> reponsesUtilisateur = new ArrayList<>();
                
                if (question.getType() == QuizQuestion.QuestionType.QCM) {
                    // Pour QCM : comparer les IDs des réponses
                    if (userAnswer != null && userAnswer.getReponseIds() != null) {
                        reponsesUtilisateur = userAnswer.getReponseIds();
                        
                        // Vérifier si toutes les bonnes réponses sont sélectionnées
                        // et qu'aucune mauvaise réponse n'est sélectionnée
                        if (reponsesUtilisateur.size() == bonnesReponseIds.size() &&
                            reponsesUtilisateur.containsAll(bonnesReponseIds)) {
                            pointsObtenus = question.getPoints();
                            correcte = true;
                        }
                    }
                } else if (question.getType() == QuizQuestion.QuestionType.TEXTE) {
                    // Pour TEXTE : comparer le texte (insensible à la casse et aux espaces)
                    if (userAnswer != null && userAnswer.getTexteReponse() != null) {
                        String reponseUtilisateur = userAnswer.getTexteReponse().trim().toLowerCase();
                        String bonneReponse = bonnesReponses.isEmpty() ? "" : 
                                bonnesReponses.get(0).getTexte().trim().toLowerCase();
                        
                        if (reponseUtilisateur.equals(bonneReponse)) {
                            pointsObtenus = question.getPoints();
                            correcte = true;
                        }
                    }
                }
                
                scoreObtenu += pointsObtenus;
                
                // Créer le détail de la question
                QuizResultDTO.QuestionResultDTO questionResult = QuizResultDTO.QuestionResultDTO.builder()
                        .questionId(question.getId())
                        .questionContenu(question.getContenu())
                        .pointsObtenus(pointsObtenus)
                        .pointsTotal(question.getPoints())
                        .correcte(correcte)
                        .reponsesCorrectes(bonnesReponseIds)
                        .reponsesUtilisateur(reponsesUtilisateur)
                        .build();
                
                detailsQuestions.add(questionResult);
            }
            
            // Calculer le pourcentage
            double pourcentage = scoreTotal > 0 ? (double) scoreObtenu / scoreTotal * 100 : 0;
            boolean reussi = quiz.getScoreMinimum() != null && pourcentage >= quiz.getScoreMinimum();
            
            // Sauvegarder la tentative
            UserQuizAttempt attempt = new UserQuizAttempt();
            attempt.setUser(user);
            attempt.setQuiz(quiz);
            attempt.setScore((double) scoreObtenu);
            attempt.setScoreTotal(scoreTotal);
            attempt.setDateTentative(LocalDateTime.now());
            attempt.setActivate(true);
            
            UserQuizAttempt savedAttempt = userQuizAttemptRepository.save(attempt);
            
            // Construire le résultat
            QuizResultDTO result = QuizResultDTO.builder()
                    .attemptId(savedAttempt.getId())
                    .quizId(quiz.getId())
                    .quizTitre(quiz.getTitre())
                    .score((double) scoreObtenu)
                    .scoreTotal(scoreTotal)
                    .pourcentage(pourcentage)
                    .reussi(reussi)
                    .dateTentative(savedAttempt.getDateTentative())
                    .detailsQuestions(detailsQuestions)
                    .build();
            
            return CResponse.success(result, "Quiz soumis avec succès");
            
        } catch (Exception e) {
            e.printStackTrace();
            return CResponse.error("Erreur lors de la soumission du quiz: " + e.getMessage());
        }
    }
    
    /**
     * Convertit une entité Quiz en DTO
     */
    private QuizDTO convertToDTO(Quiz quiz) {
        QuizDTO dto = new QuizDTO();
        dto.setId(quiz.getId());
        dto.setTitre(quiz.getTitre());
        dto.setDescription(quiz.getDescription());
        dto.setCourseId(quiz.getCourse() != null ? quiz.getCourse().getId() : null);
        dto.setDureeMinutes(quiz.getDureeMinutes());
        dto.setScoreMinimum(quiz.getScoreMinimum());
        
        if (quiz.getQuestions() != null) {
            List<QuizDTO.QuestionDTO> questionDTOs = quiz.getQuestions().stream().map(q -> {
                QuizDTO.QuestionDTO questionDTO = new QuizDTO.QuestionDTO();
                questionDTO.setId(q.getId());
                questionDTO.setContenu(q.getContenu());
                questionDTO.setType(q.getType());
                questionDTO.setPoints(q.getPoints());
                
                if (q.getReponses() != null) {
                    List<QuizDTO.ReponseDTO> reponseDTOs = q.getReponses().stream().map(r -> {
                        QuizDTO.ReponseDTO reponseDTO = new QuizDTO.ReponseDTO();
                        reponseDTO.setId(r.getId());
                        reponseDTO.setTexte(r.getTexte());
                        reponseDTO.setEstCorrecte(r.getEstCorrecte());
                        return reponseDTO;
                    }).collect(Collectors.toList());
                    questionDTO.setReponses(reponseDTOs);
                }
                
                return questionDTO;
            }).collect(Collectors.toList());
            dto.setQuestions(questionDTOs);
        }
        
        return dto;
    }
}
