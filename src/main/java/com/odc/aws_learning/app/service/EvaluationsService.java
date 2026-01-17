package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Evaluations;
import com.odc.aws_learning.app.entity.EvaluationAttempt;
import com.odc.aws_learning.app.entity.Questions;
import com.odc.aws_learning.app.entity.Reponses;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Certificate;
import com.odc.aws_learning.app.repository.EvaluationsRepository;
import com.odc.aws_learning.app.repository.EvaluationAttemptRepository;
import com.odc.aws_learning.app.repository.QuestionsRepository;
import com.odc.aws_learning.app.repository.ReponsesRepository;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.repository.CertificateRepository;
import com.odc.aws_learning.app.dto.EvaluationRequest;
import com.odc.aws_learning.app.dto.EvaluationSubmissionRequest;
import com.odc.aws_learning.app.dto.EvaluationCorrectionRequest;
import com.odc.aws_learning.app.wrapper.Quiz_Answer;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EvaluationsService {
    private final EvaluationsRepository evaluationsRepository;
    private final QuestionsRepository questionsRepository;
    private final ReponsesRepository reponsesRepository;
    private final EvaluationAttemptRepository evaluationAttemptRepository;
    private final CoursesRepository coursesRepository;
    private final CertificateRepository certificateRepository;
    private final CertificateService certificateService;
    public CResponse<?> saveEvaluations(Evaluations evaluations) {
        try {
            Evaluations evaluations1 = evaluationsRepository.save(evaluations);
            return CResponse.success(evaluations1, "Evalution enregistré avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur d'enregistrement");
        }
    }

    public CResponse<?> getAll() {
        try {
            List<Evaluations> evaluations = evaluationsRepository.findAll();
            return CResponse.success(evaluations, "Les evaluations");
        } catch (Exception e) {
            return CResponse.error("Erreur de récupération");
        }
    }
    public CResponse<?> createEvaluation(Quiz_Answer quiz_answer){
        try {
            Evaluations evaluation = new Evaluations();
            evaluation.setTitle(quiz_answer.getEvaluationTitle()
            );
            Evaluations evaliuationSave = evaluationsRepository.save(evaluation);
            quiz_answer.getQuestionsList().forEach(quiz -> {
                Questions question = new Questions();
                question.setTitle(quiz.getTitle());
                question.setDescription(quiz.getDescription());
                question.setStatus(quiz.getStatus());
                question.setImagePath(quiz.getImagePath());
                question.setType(quiz.getType());
                quiz.setEvaluations(evaliuationSave);
                Questions questionSave = questionsRepository.save(quiz);
                quiz.getReponses().forEach(answerR ->{
                    answerR.setQuestions(questionSave);
                    reponsesRepository.save(answerR);
                });
            });

            return CResponse.success(evaliuationSave, "Evaluation enregistré avec succes");

        }catch (Exception e){
            return CResponse.error("Erreur d'enregistrement");
        }
    }
    
    /**
     * Créer une nouvelle évaluation (instructeur)
     */
    @Transactional
    public CResponse<?> createEvaluation(EvaluationRequest request, User instructor) {
        try {
            Optional<Courses> courseOpt = coursesRepository.findById(request.getCourseId());
            if (courseOpt.isEmpty()) {
                return CResponse.error("Cours introuvable");
            }
            
            Evaluations evaluation = new Evaluations();
            evaluation.setTitle(request.getTitle());
            evaluation.setDescription(request.getDescription());
            evaluation.setType(request.getType());
            evaluation.setCourse(courseOpt.get());
            evaluation.setInstructor(instructor);
            evaluation.setStatus("ACTIVE");
            
            if (request.getType() == Evaluations.EvaluationType.TP) {
                evaluation.setTpInstructions(request.getTpInstructions());
                evaluation.setTpFileUrl(request.getTpFileUrl());
            }
            
            Evaluations saved = evaluationsRepository.save(evaluation);
            return CResponse.success(saved, "Évaluation créée avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la création de l'évaluation: " + e.getMessage());
        }
    }
    
    /**
     * Soumettre une évaluation (apprenant)
     * - Pour QUIZ: calcule automatiquement le score
     * - Pour TP: crée une tentative en attente de correction
     */
    @Transactional
    public CResponse<?> submitEvaluation(EvaluationSubmissionRequest request, User learner) {
        try {
            Optional<Evaluations> evalOpt = evaluationsRepository.findById(request.getEvaluationId());
            if (evalOpt.isEmpty()) {
                return CResponse.error("Évaluation introuvable");
            }
            
            Evaluations evaluation = evalOpt.get();
            EvaluationAttempt attempt = new EvaluationAttempt(evaluation, learner);
            
            if (evaluation.getType() == Evaluations.EvaluationType.QUIZ) {
                // Calculer le score automatiquement
                Double score = calculateQuizScore(evaluation, request.getAnswers(), request.getTextAnswers());
                attempt.setScore(score);
                attempt.setStatus(score >= 70.0 ? EvaluationAttempt.AttemptStatus.PASSED : EvaluationAttempt.AttemptStatus.FAILED);
                
                // Vérifier si certificat doit être généré
                if (score >= 70.0) {
                    checkAndGenerateCertificate(learner, evaluation.getCourse(), score);
                }
            } else if (evaluation.getType() == Evaluations.EvaluationType.TP) {
                // TP: en attente de correction par l'instructeur
                attempt.setSubmittedFileUrl(request.getSubmittedFileUrl());
                attempt.setStatus(EvaluationAttempt.AttemptStatus.PENDING);
            }
            
            EvaluationAttempt saved = evaluationAttemptRepository.save(attempt);
            return CResponse.success(saved, "Évaluation soumise avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la soumission: " + e.getMessage());
        }
    }
    
    /**
     * Corriger une évaluation TP (instructeur)
     */
    @Transactional
    public CResponse<?> correctEvaluation(EvaluationCorrectionRequest request, User instructor) {
        try {
            Optional<EvaluationAttempt> attemptOpt = evaluationAttemptRepository.findById(request.getAttemptId());
            if (attemptOpt.isEmpty()) {
                return CResponse.error("Tentative d'évaluation introuvable");
            }
            
            EvaluationAttempt attempt = attemptOpt.get();
            if (attempt.getEvaluation().getType() != Evaluations.EvaluationType.TP) {
                return CResponse.error("Cette évaluation n'est pas un TP");
            }
            
            if (attempt.getStatus() != EvaluationAttempt.AttemptStatus.PENDING) {
                return CResponse.error("Cette évaluation a déjà été corrigée");
            }
            
            // Valider le score (0-100)
            if (request.getScore() < 0 || request.getScore() > 100) {
                return CResponse.error("Le score doit être entre 0 et 100");
            }
            
            attempt.setScore(request.getScore());
            attempt.setCorrectedBy(instructor);
            attempt.setCorrectedAt(java.time.Instant.now());
            attempt.setInstructorFeedback(request.getFeedback());
            
            // Mettre à jour le statut selon le score
            if (request.getScore() >= 70.0) {
                attempt.setStatus(EvaluationAttempt.AttemptStatus.PASSED);
                // Générer le certificat automatiquement
                checkAndGenerateCertificate(attempt.getUser(), attempt.getEvaluation().getCourse(), request.getScore());
            } else {
                attempt.setStatus(EvaluationAttempt.AttemptStatus.FAILED);
            }
            
            EvaluationAttempt saved = evaluationAttemptRepository.save(attempt);
            return CResponse.success(saved, "Évaluation corrigée avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la correction: " + e.getMessage());
        }
    }
    
    /**
     * Calculer le score pour une évaluation QUIZ
     */
    private Double calculateQuizScore(Evaluations evaluation, Map<Long, Long> answers, Map<Long, String> textAnswers) {
        if (evaluation.getQuestions() == null || evaluation.getQuestions().isEmpty()) {
            return 0.0;
        }
        
        int totalQuestions = evaluation.getQuestions().size();
        int correctAnswers = 0;
        
        for (Questions question : evaluation.getQuestions()) {
            boolean isCorrect = false;
            
            if (answers != null && answers.containsKey(question.getId())) {
                // Réponse à choix multiple
                Long selectedResponseId = answers.get(question.getId());
                if (question.getReponses() != null) {
                    for (Reponses response : question.getReponses()) {
                        if (response.getId().equals(selectedResponseId) && response.getIsCorrect() != null && response.getIsCorrect()) {
                            isCorrect = true;
                            break;
                        }
                    }
                }
            } else if (textAnswers != null && textAnswers.containsKey(question.getId())) {
                // Réponse texte (comparaison simple - peut être améliorée)
                String submittedAnswer = textAnswers.get(question.getId()).trim().toLowerCase();
                if (question.getReponses() != null) {
                    for (Reponses response : question.getReponses()) {
                        if (response.getIsCorrect() != null && response.getIsCorrect()) {
                            String correctAnswer = response.getTitle() != null ? response.getTitle().trim().toLowerCase() : "";
                            if (submittedAnswer.equals(correctAnswer)) {
                                isCorrect = true;
                                break;
                            }
                        }
                    }
                }
            }
            
            if (isCorrect) {
                correctAnswers++;
            }
        }
        
        // Calculer le pourcentage
        return (double) (correctAnswers * 100) / totalQuestions;
    }
    
    /**
     * Vérifier et générer un certificat si score >= 70% et pas déjà généré
     */
    private void checkAndGenerateCertificate(User user, Courses course, Double score) {
        try {
            // Vérifier si un certificat existe déjà pour ce cours et cet utilisateur
            List<Certificate> existingCertificates = certificateRepository.findAll().stream()
                .filter(cert -> cert.getUser().getId().equals(user.getId()) 
                    && cert.getCourse().getId().equals(course.getId()))
                .collect(java.util.stream.Collectors.toList());
            
            if (!existingCertificates.isEmpty()) {
                // Certificat déjà généré
                return;
            }
            
            // Générer le certificat
            certificateService.generateCertificateForEvaluation(user, course, score);
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération automatique du certificat: " + e.getMessage());
            // Ne pas faire échouer la soumission/correction si la génération du certificat échoue
        }
    }
    
    /**
     * Récupérer les tentatives d'un apprenant pour une évaluation
     */
    public CResponse<?> getAttemptsByEvaluationAndUser(Long evaluationId, Long userId) {
        try {
            List<EvaluationAttempt> attempts = evaluationAttemptRepository
                .findByEvaluationIdAndUserIdOrderByCreatedAtDesc(evaluationId, userId);
            return CResponse.success(attempts, "Tentatives récupérées avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération: " + e.getMessage());
        }
    }
    
    /**
     * Récupérer les évaluations en attente de correction pour un instructeur
     */
    public CResponse<?> getPendingEvaluationsForInstructor(Long instructorId) {
        try {
            List<EvaluationAttempt> pending = evaluationAttemptRepository.findPendingAttemptsByInstructor(instructorId);
            return CResponse.success(pending, "Évaluations en attente récupérées avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération: " + e.getMessage());
        }
    }
}
