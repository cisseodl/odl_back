package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Evaluations;
import com.odc.aws_learning.app.entity.EvaluationAttempt;
import com.odc.aws_learning.app.entity.Questions;
import com.odc.aws_learning.app.entity.Reponses;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Certificate;
import com.odc.aws_learning.app.entity.Lesson;
import com.odc.aws_learning.app.repository.EvaluationsRepository;
import com.odc.aws_learning.app.repository.EvaluationAttemptRepository;
import com.odc.aws_learning.app.repository.QuestionsRepository;
import com.odc.aws_learning.app.repository.ReponsesRepository;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.repository.CertificateRepository;
import com.odc.aws_learning.app.repository.CourseSatisfactionRepository;
import com.odc.aws_learning.app.entity.CourseSatisfaction;
import com.odc.aws_learning.app.repository.LessonRepository;
import com.odc.aws_learning.app.dto.EvaluationRequest;
import com.odc.aws_learning.app.dto.EvaluationSubmissionRequest;
import com.odc.aws_learning.app.dto.EvaluationCorrectionRequest;
import com.odc.aws_learning.app.wrapper.Quiz_Answer;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

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
    private final CourseSatisfactionRepository courseSatisfactionRepository;
    private final LessonRepository lessonRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public CResponse<?> saveEvaluations(Evaluations evaluations) {
        try {
            Evaluations evaluations1 = evaluationsRepository.save(evaluations);
            return CResponse.success(evaluations1, "Evalution enregistré avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur d'enregistrement");
        }
    }

    /**
     * @param courseLevelOnly si true, ne retourne que les évaluations de niveau cours (lesson null),
     *                        i.e. examens de fin de cours, pas les quiz/TD associés à une leçon
     * @param currentUser si non null et instructeur, ne retourne que les évaluations de cet instructeur
     */
    public CResponse<?> getAll(boolean courseLevelOnly, User currentUser) {
        try {
            boolean filterByInstructor = currentUser != null && currentUser.getInstructor() != null;
            Long instructorUserId = filterByInstructor ? currentUser.getId() : null;
            List<Evaluations> evaluations;
            if (courseLevelOnly) {
                evaluations = filterByInstructor && instructorUserId != null
                        ? evaluationsRepository.findAllCourseLevelWithCourseAndInstructorByInstructorId(instructorUserId)
                        : evaluationsRepository.findAllCourseLevelWithCourseAndInstructor();
            } else {
                evaluations = filterByInstructor && instructorUserId != null
                        ? evaluationsRepository.findAllWithLessonModuleAndCourseByInstructorId(instructorUserId)
                        : evaluationsRepository.findAllWithLessonModuleAndCourse();
            }
            return CResponse.success(evaluations, "Les evaluations");
        } catch (Exception e) {
            return CResponse.error("Erreur de récupération");
        }
    }

    /**
     * Récupère une évaluation par id avec ses questions et réponses (pour le dashboard instructeur).
     */
    @Transactional(readOnly = true)
    public CResponse<?> getEvaluationWithQuestions(Long id) {
        try {
            Optional<Evaluations> evalOpt = evaluationsRepository.findById(id);
            if (evalOpt.isEmpty()) {
                return CResponse.error("Évaluation introuvable");
            }
            Evaluations evaluation = evalOpt.get();
            // Forcer le chargement des questions et réponses (éviter LazyInitializationException)
            if (evaluation.getQuestions() != null) {
                for (Questions q : evaluation.getQuestions()) {
                    if (q.getReponses() != null) {
                        q.getReponses().size();
                    }
                }
            }
            return CResponse.success(evaluation, "Évaluation récupérée avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération: " + e.getMessage());
        }
    }

    /**
     * Récupère l'examen de fin de cours (certification).
     * Si examId est fourni, retourne cette évaluation si elle appartient au cours et est un QUIZ.
     * Sinon : premier QUIZ sans leçon, puis fallback sur le premier QUIZ du cours.
     */
    @Transactional(readOnly = true)
    public CResponse<?> getCourseExam(Long courseId, User user, Long examId) {
        try {
            if (examId != null) {
                Optional<Evaluations> evalOpt = evaluationsRepository.findByIdWithQuestions(examId);
                if (evalOpt.isPresent()) {
                    Evaluations exam = evalOpt.get();
                    if (exam.getCourse() != null && exam.getCourse().getId().equals(courseId)
                            && "QUIZ".equals(exam.getType())) {
                        // Initialiser les réponses (un seul JOIN FETCH en requête pour éviter MultipleBagFetchException)
                        if (exam.getQuestions() != null) {
                            for (Questions q : exam.getQuestions()) {
                                if (q.getReponses() != null) {
                                    q.getReponses().size();
                                }
                            }
                        }
                        return CResponse.success(exam, "Examen récupéré avec succès");
                    }
                }
            }
            List<Evaluations> exams = evaluationsRepository.findCourseExamsByCourseId(courseId);
            if (exams == null || exams.isEmpty()) {
                exams = evaluationsRepository.findQuizByCourseId(courseId);
                if (exams == null) {
                    exams = java.util.Collections.emptyList();
                }
            }
            if (exams == null || exams.isEmpty()) {
                return CResponse.error("Aucun examen disponible pour ce cours");
            }
            Evaluations exam = exams.get(0);
            if (exam.getQuestions() != null) {
                for (Questions q : exam.getQuestions()) {
                    if (q.getReponses() != null) {
                        q.getReponses().size();
                    }
                }
            }
            return CResponse.success(exam, "Examen récupéré avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération de l'examen: " + e.getMessage());
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
            
            // Quiz associé à une leçon (lessonId fourni) → affiché dans liste Quiz. Évaluation niveau cours (pas de lessonId) → liste Évaluations.
            if (request.getLessonId() != null) {
                Optional<Lesson> lessonOptional = lessonRepository.findById(request.getLessonId());
                if (lessonOptional.isPresent()) {
                    evaluation.setLesson(lessonOptional.get());
                } else {
                    evaluation.setLesson(null);
                }
            } else {
                evaluation.setLesson(null); // Examen de fin de cours (niveau cours)
            }
            
            if (request.getType() == Evaluations.EvaluationType.TP) {
                evaluation.setTpInstructions(request.getTpInstructions());
                evaluation.setTpFileUrl(request.getTpFileUrl());
            }
            
            Evaluations saved = evaluationsRepository.save(evaluation);
            
            // Si c'est un QUIZ et qu'il y a des questions, les créer
            if (request.getType() == Evaluations.EvaluationType.QUIZ && request.getQuestions() != null && !request.getQuestions().isEmpty()) {
                for (com.odc.aws_learning.app.dto.QuestionRequest questionReq : request.getQuestions()) {
                    Questions question = new Questions();
                    question.setTitle(questionReq.getTitle());
                    question.setDescription(questionReq.getDescription());
                    question.setType(questionReq.getType() != null ? questionReq.getType() : "SINGLE_CHOICE");
                    question.setStatus("ACTIVE");
                    question.setEvaluations(saved);
                    if (questionReq.getPoints() != null && questionReq.getPoints() > 0) {
                        question.setPoints(questionReq.getPoints());
                    }
                    
                    Questions savedQuestion = questionsRepository.save(question);
                    
                    // Créer les réponses pour cette question
                    if (questionReq.getReponses() != null && !questionReq.getReponses().isEmpty()) {
                        for (com.odc.aws_learning.app.dto.ResponseRequest responseReq : questionReq.getReponses()) {
                            Reponses response = new Reponses();
                            response.setTitle(responseReq.getTitle());
                            response.setDescription(responseReq.getDescription());
                            response.setStatus("ACTIVE");
                            response.setIsCorrect(responseReq.getIsCorrect() != null ? responseReq.getIsCorrect() : false);
                            response.setQuestions(savedQuestion);
                            reponsesRepository.save(response);
                        }
                    }
                }
            }
            
            return CResponse.success(saved, "Évaluation créée avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la création de l'évaluation: " + e.getMessage());
        }
    }
    
    /**
     * Soumettre une évaluation (apprenant)
     * - Pour QUIZ: crée une tentative avec statut PENDING (score calculé après satisfaction)
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
            
            // Vérifier si l'apprenant est inscrit au cours
            // TODO: Ajouter vérification d'inscription si nécessaire
            
            // Vérifier s'il existe déjà une tentative (autoriser une nouvelle si la dernière a échoué)
            List<EvaluationAttempt> existingAttempts = evaluationAttemptRepository
                    .findByEvaluationIdAndUserIdOrderByCreatedAtDesc(request.getEvaluationId(), learner.getId());
            if (!existingAttempts.isEmpty()) {
                EvaluationAttempt lastAttempt = existingAttempts.get(0);
                if (lastAttempt.getStatus() != EvaluationAttempt.AttemptStatus.FAILED) {
                    return CResponse.error("Vous avez déjà soumis cet examen");
                }
                // Si échec, on autorise une nouvelle tentative
            }
            
            EvaluationAttempt attempt = new EvaluationAttempt(evaluation, learner);
            if (request.getCertificateDisplayName() != null && !request.getCertificateDisplayName().isBlank()) {
                attempt.setCertificateDisplayName(request.getCertificateDisplayName().trim());
            }
            if (request.getCertificateEmail() != null && !request.getCertificateEmail().isBlank()) {
                attempt.setCertificateEmail(request.getCertificateEmail().trim());
            }
            
            if (evaluation.getType() == Evaluations.EvaluationType.QUIZ) {
                // Pour les QUIZ, on ne calcule PAS le score maintenant
                // Le score sera calculé après la soumission de la satisfaction
                // Stocker les réponses dans instructorFeedback temporairement (sera remplacé par la satisfaction)
                try {
                    String answersJson = objectMapper.writeValueAsString(request.getAnswers());
                    attempt.setInstructorFeedback("ANSWERS:" + answersJson); // Stockage temporaire
                } catch (JsonProcessingException e) {
                    // En cas d'erreur, continuer sans stocker les réponses
                }
                attempt.setStatus(EvaluationAttempt.AttemptStatus.PENDING);
                // Le score reste null pour l'instant
            } else if (evaluation.getType() == Evaluations.EvaluationType.TP) {
                // TP: en attente de correction (fichier et/ou texte selon instructions de l'instructeur)
                boolean hasFile = request.getSubmittedFileUrl() != null && !request.getSubmittedFileUrl().isBlank();
                boolean hasText = request.getSubmittedText() != null && !request.getSubmittedText().isBlank();
                if (!hasFile && !hasText) {
                    return CResponse.error("Veuillez déposer un fichier ou saisir votre réponse en texte.");
                }
                if (hasFile) attempt.setSubmittedFileUrl(request.getSubmittedFileUrl());
                if (hasText) attempt.setSubmittedText(request.getSubmittedText());
                attempt.setStatus(EvaluationAttempt.AttemptStatus.PENDING);
            }
            
            EvaluationAttempt saved = evaluationAttemptRepository.save(attempt);
            return CResponse.success(saved, "Évaluation soumise avec succès. Veuillez remplir le formulaire de satisfaction pour voir vos résultats.");
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
     * Calculer le score pour une évaluation QUIZ.
     * Les réponses peuvent être envoyées avec la clé = ID de la question ou clé = index (0, 1, 2...).
     * Si des points sont définis par question, le score = (points obtenus / points totaux) * 100,
     * sinon (nombre de bonnes réponses / nombre de questions) * 100.
     */
    private Double calculateQuizScore(Evaluations evaluation, Map<Long, Long> answers, Map<Long, String> textAnswers) {
        if (evaluation.getQuestions() == null || evaluation.getQuestions().isEmpty()) {
            return 0.0;
        }
        
        List<Questions> questionList = evaluation.getQuestions();
        int totalQuestions = questionList.size();
        int correctAnswers = 0;
        int totalPoints = 0;
        int obtainedPoints = 0;
        boolean usePoints = false;
        
        for (int i = 0; i < questionList.size(); i++) {
            Questions question = questionList.get(i);
            // Clé possible : ID de la question (backend) ou index (front envoie 0, 1, 2...)
            Long answerKeyById = question.getId();
            Long answerKeyByIndex = Long.valueOf(i);
            Long selectedResponseId = null;
            String submittedText = null;
            
            if (answers != null) {
                Long rawVal = null;
                if (answers.containsKey(answerKeyById)) {
                    rawVal = answers.get(answerKeyById);
                } else if (answers.containsKey(answerKeyByIndex)) {
                    Object val = answers.get(answerKeyByIndex);
                    if (val instanceof Number) {
                        rawVal = ((Number) val).longValue();
                    }
                }
                selectedResponseId = rawVal;
            }
            if (textAnswers != null && selectedResponseId == null) {
                if (textAnswers.containsKey(answerKeyById)) {
                    submittedText = textAnswers.get(answerKeyById);
                } else if (textAnswers.containsKey(answerKeyByIndex)) {
                    submittedText = textAnswers.get(answerKeyByIndex);
                }
            }
            
            boolean isCorrect = false;
            int questionPoints = (question.getPoints() != null && question.getPoints() > 0) ? question.getPoints() : 1;
            if (totalPoints >= 0 && questionPoints > 0) {
                totalPoints += questionPoints;
                usePoints = usePoints || (question.getPoints() != null && question.getPoints() > 0);
            }
            
            if (selectedResponseId != null && question.getReponses() != null) {
                for (Reponses response : question.getReponses()) {
                    if (response.getId().equals(selectedResponseId) && Boolean.TRUE.equals(response.getIsCorrect())) {
                        isCorrect = true;
                        break;
                    }
                }
            } else if (submittedText != null && submittedText.trim().length() > 0 && question.getReponses() != null) {
                String submitted = submittedText.trim().toLowerCase();
                for (Reponses response : question.getReponses()) {
                    if (Boolean.TRUE.equals(response.getIsCorrect()) && response.getTitle() != null) {
                        if (submitted.equals(response.getTitle().trim().toLowerCase())) {
                            isCorrect = true;
                            break;
                        }
                    }
                }
            }
            
            if (isCorrect) {
                correctAnswers++;
                obtainedPoints += questionPoints;
            }
        }
        
        if (usePoints && totalPoints > 0) {
            return (double) (obtainedPoints * 100) / totalPoints;
        }
        return totalQuestions > 0 ? (double) (correctAnswers * 100) / totalQuestions : 0.0;
    }
    
    /**
     * Vérifier et générer un certificat si score >= 70% et pas déjà généré
     */
    private void checkAndGenerateCertificate(User user, Courses course, Double score) {
        try {
            List<Certificate> existingCertificates = certificateRepository.findAll().stream()
                .filter(cert -> cert.getUser().getId().equals(user.getId())
                    && cert.getCourse().getId().equals(course.getId()))
                .collect(java.util.stream.Collectors.toList());
            if (!existingCertificates.isEmpty()) return;
            certificateService.generateCertificateForEvaluation(user, course, score);
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération automatique du certificat: " + e.getMessage());
        }
    }

    /** Génère le certificat à partir de la tentative (nom/email saisis par l'apprenant avant l'examen). */
    private void checkAndGenerateCertificate(EvaluationAttempt attempt) {
        try {
            if (attempt.getScore() == null || attempt.getScore() < 70.0) return;
            List<Certificate> existingCertificates = certificateRepository.findAll().stream()
                .filter(cert -> cert.getUser().getId().equals(attempt.getUser().getId())
                    && cert.getCourse().getId().equals(attempt.getEvaluation().getCourse().getId()))
                .collect(java.util.stream.Collectors.toList());
            if (!existingCertificates.isEmpty()) return;
            certificateService.generateCertificateForEvaluation(attempt);
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du certificat (tentative): " + e.getMessage());
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
    
    /**
     * Récupérer les évaluations corrigées par un instructeur
     */
    public CResponse<?> getCorrectedEvaluationsForInstructor(Long instructorId) {
        try {
            List<EvaluationAttempt> corrected = evaluationAttemptRepository.findCorrectedAttemptsByInstructor(instructorId);
            return CResponse.success(corrected, "Évaluations corrigées récupérées avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération: " + e.getMessage());
        }
    }

    /**
     * Soumettre la satisfaction de l'apprenant après avoir soumis l'examen
     * Calcule le score et met à jour la tentative
     */
    @Transactional
    public CResponse<?> submitSatisfaction(Long attemptId, User learner, String satisfaction, Integer rating) {
        try {
            Optional<EvaluationAttempt> attemptOpt = evaluationAttemptRepository.findById(attemptId);
            if (attemptOpt.isEmpty()) {
                return CResponse.error("Tentative d'examen non trouvée");
            }

            EvaluationAttempt attempt = attemptOpt.get();

            // Vérifier que la tentative appartient à l'utilisateur
            if (!attempt.getUser().getId().equals(learner.getId())) {
                return CResponse.error("Cette tentative ne vous appartient pas");
            }

            // Vérifier que la tentative est en PENDING (pas encore de score)
            if (attempt.getStatus() != EvaluationAttempt.AttemptStatus.PENDING || attempt.getScore() != null) {
                return CResponse.error("Cette évaluation a déjà été traitée");
            }

            // Vérifier si la satisfaction a déjà été soumise
            Optional<CourseSatisfaction> existingSatisfaction = courseSatisfactionRepository
                    .findByEvaluationAttemptId(attemptId);
            if (existingSatisfaction.isPresent()) {
                return CResponse.error("Vous avez déjà soumis votre satisfaction pour cet examen");
            }

            // Enregistrer la satisfaction
            CourseSatisfaction courseSatisfaction = new CourseSatisfaction(
                    attempt.getEvaluation().getCourse(),
                    learner,
                    attempt,
                    satisfaction
            );
            if (rating != null) {
                courseSatisfaction.setRating(rating);
            }
            courseSatisfactionRepository.save(courseSatisfaction);

            // Maintenant, calculer le score de l'examen
            // Récupérer les réponses stockées dans instructorFeedback
            Map<Long, Long> answers = null;
            Map<Long, String> textAnswers = null;
            if (attempt.getInstructorFeedback() != null && attempt.getInstructorFeedback().startsWith("ANSWERS:")) {
                try {
                    String answersJson = attempt.getInstructorFeedback().substring(8); // Enlever "ANSWERS:"
                    @SuppressWarnings("unchecked")
                    Map<String, Object> answersMap = objectMapper.readValue(answersJson, Map.class);
                    // Convertir les clés String en Long
                    answers = new HashMap<>();
                    for (Map.Entry<String, Object> entry : answersMap.entrySet()) {
                        try {
                            Long questionId = Long.parseLong(entry.getKey());
                            Long responseId = entry.getValue() instanceof Number 
                                ? ((Number) entry.getValue()).longValue() 
                                : Long.parseLong(entry.getValue().toString());
                            answers.put(questionId, responseId);
                        } catch (NumberFormatException e) {
                            // Ignorer les entrées invalides
                        }
                    }
                } catch (Exception e) {
                    // En cas d'erreur, continuer sans les réponses
                }
            }

            // Calculer le score
            Double score = calculateQuizScore(attempt.getEvaluation(), answers, textAnswers);
            attempt.setScore(score);
            attempt.setInstructorFeedback(null); // Nettoyer le champ temporaire

            // Mettre à jour le statut
            if (score >= 70.0) {
                attempt.setStatus(EvaluationAttempt.AttemptStatus.PASSED);
                // Générer le certificat si le score est suffisant (nom/email saisis avant l'examen)
                checkAndGenerateCertificate(attempt);
            } else {
                attempt.setStatus(EvaluationAttempt.AttemptStatus.FAILED);
            }

            evaluationAttemptRepository.save(attempt);

            return CResponse.success(attempt, "Satisfaction enregistrée. Vos résultats sont maintenant disponibles.");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la soumission de la satisfaction: " + e.getMessage());
        }
    }

    /**
     * Récupère la dernière tentative de l'utilisateur pour une évaluation (examen).
     */
    @Transactional(readOnly = true)
    public CResponse<?> getLatestAttemptForExam(Long evaluationId, User user) {
        try {
            List<EvaluationAttempt> attempts = evaluationAttemptRepository
                    .findByEvaluationIdAndUserIdOrderByCreatedAtDesc(evaluationId, user.getId());
            if (attempts == null || attempts.isEmpty()) {
                return CResponse.error("Aucune tentative trouvée pour cet examen");
            }
            return CResponse.success(attempts.get(0), "Dernière tentative récupérée");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération: " + e.getMessage());
        }
    }

    /**
     * Récupère les résultats d'un examen (après soumission de la satisfaction)
     */
    @Transactional(readOnly = true)
    public CResponse<?> getExamResults(Long attemptId, User user) {
        try {
            Optional<EvaluationAttempt> attemptOpt = evaluationAttemptRepository.findById(attemptId);
            if (attemptOpt.isEmpty()) {
                return CResponse.error("Tentative d'examen non trouvée");
            }

            EvaluationAttempt attempt = attemptOpt.get();

            // Vérifier que la tentative appartient à l'utilisateur
            if (!attempt.getUser().getId().equals(user.getId())) {
                return CResponse.error("Cette tentative ne vous appartient pas");
            }

            // Vérifier que la satisfaction a été soumise
            Optional<CourseSatisfaction> satisfaction = courseSatisfactionRepository
                    .findByEvaluationAttemptId(attemptId);
            if (satisfaction.isEmpty()) {
                return CResponse.error("Vous devez d'abord soumettre votre satisfaction pour voir les résultats");
            }

            return CResponse.success(attempt, "Résultats de l'examen récupérés avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la récupération des résultats: " + e.getMessage());
        }
    }
    
    /**
     * Supprimer une évaluation (TD ou Quiz)
     * Supprime également toutes les questions, réponses et tentatives associées
     */
    @Transactional
    public CResponse<?> deleteEvaluation(Long id, User user) {
        try {
            Optional<Evaluations> evalOpt = evaluationsRepository.findById(id);
            if (evalOpt.isEmpty()) {
                return CResponse.error("Évaluation introuvable");
            }
            
            Evaluations evaluation = evalOpt.get();
            
            // Vérifier que l'utilisateur est l'instructeur ou un admin
            boolean isAdmin = user.getAdmin() != null;
            boolean isInstructor = user.getInstructor() != null;
            boolean isOwner = evaluation.getInstructor() != null && evaluation.getInstructor().getId().equals(user.getId());
            
            if (!isAdmin && (!isInstructor || !isOwner)) {
                return CResponse.error("Vous n'êtes pas autorisé à supprimer cette évaluation");
            }
            
            // Supprimer toutes les tentatives associées
            List<EvaluationAttempt> attempts = evaluationAttemptRepository.findByEvaluation(evaluation);
            if (attempts != null && !attempts.isEmpty()) {
                evaluationAttemptRepository.deleteAll(attempts);
            }
            
            // Supprimer toutes les questions et leurs réponses
            if (evaluation.getQuestions() != null && !evaluation.getQuestions().isEmpty()) {
                for (Questions question : evaluation.getQuestions()) {
                    // Supprimer les réponses
                    if (question.getReponses() != null && !question.getReponses().isEmpty()) {
                        reponsesRepository.deleteAll(question.getReponses());
                    }
                    // Supprimer la question
                    questionsRepository.delete(question);
                }
            }
            
            // Supprimer l'évaluation
            evaluationsRepository.delete(evaluation);
            
            return CResponse.success(null, "Évaluation supprimée avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    /**
     * Mettre à jour une évaluation (titre, description, type, cours, questions avec points).
     * Pour les QUIZ, les questions existantes sont supprimées et recréées à partir de la requête.
     */
    @Transactional
    public CResponse<?> updateEvaluation(Long id, EvaluationRequest request, User user) {
        try {
            Optional<Evaluations> evalOpt = evaluationsRepository.findById(id);
            if (evalOpt.isEmpty()) {
                return CResponse.error("Évaluation introuvable");
            }
            Evaluations evaluation = evalOpt.get();
            boolean isAdmin = user.getAdmin() != null;
            boolean isOwner = evaluation.getInstructor() != null && evaluation.getInstructor().getId().equals(user.getId());
            if (!isAdmin && !isOwner) {
                return CResponse.error("Vous n'êtes pas autorisé à modifier cette évaluation");
            }
            Optional<Courses> courseOpt = coursesRepository.findById(request.getCourseId());
            if (courseOpt.isEmpty()) {
                return CResponse.error("Cours introuvable");
            }
            evaluation.setTitle(request.getTitle());
            evaluation.setDescription(request.getDescription());
            evaluation.setType(request.getType());
            evaluation.setCourse(courseOpt.get());
            if (request.getType() == Evaluations.EvaluationType.TP) {
                evaluation.setTpInstructions(request.getTpInstructions());
                evaluation.setTpFileUrl(request.getTpFileUrl());
            }
            if (request.getLessonId() != null) {
                Optional<Lesson> lessonOpt = lessonRepository.findById(request.getLessonId());
                evaluation.setLesson(lessonOpt.orElse(null));
            } else {
                evaluation.setLesson(null);
            }
            Evaluations saved = evaluationsRepository.save(evaluation);

            if (request.getType() == Evaluations.EvaluationType.QUIZ && evaluation.getQuestions() != null && !evaluation.getQuestions().isEmpty()) {
                for (Questions question : evaluation.getQuestions()) {
                    if (question.getReponses() != null && !question.getReponses().isEmpty()) {
                        reponsesRepository.deleteAll(question.getReponses());
                    }
                    questionsRepository.delete(question);
                }
            }
            if (request.getType() == Evaluations.EvaluationType.QUIZ && request.getQuestions() != null && !request.getQuestions().isEmpty()) {
                for (com.odc.aws_learning.app.dto.QuestionRequest questionReq : request.getQuestions()) {
                    Questions question = new Questions();
                    question.setTitle(questionReq.getTitle());
                    question.setDescription(questionReq.getDescription());
                    question.setType(questionReq.getType() != null ? questionReq.getType() : "SINGLE_CHOICE");
                    question.setStatus("ACTIVE");
                    question.setEvaluations(saved);
                    if (questionReq.getPoints() != null && questionReq.getPoints() > 0) {
                        question.setPoints(questionReq.getPoints());
                    }
                    Questions savedQuestion = questionsRepository.save(question);
                    if (questionReq.getReponses() != null && !questionReq.getReponses().isEmpty()) {
                        for (com.odc.aws_learning.app.dto.ResponseRequest responseReq : questionReq.getReponses()) {
                            Reponses response = new Reponses();
                            response.setTitle(responseReq.getTitle());
                            response.setDescription(responseReq.getDescription());
                            response.setStatus("ACTIVE");
                            response.setIsCorrect(responseReq.getIsCorrect() != null ? responseReq.getIsCorrect() : false);
                            response.setQuestions(savedQuestion);
                            reponsesRepository.save(response);
                        }
                    }
                }
            }
            return CResponse.success(saved, "Évaluation mise à jour avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }
}
