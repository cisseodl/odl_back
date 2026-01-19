package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.service.LearnerLessonService;
import com.odc.aws_learning.app.repository.UserProgressRepository;
import com.odc.aws_learning.app.repository.LessonRepository;
import com.odc.aws_learning.app.repository.QuizRepository;
import com.odc.aws_learning.app.repository.UserQuizAttemptRepository;
import com.odc.aws_learning.app.repository.DetailsCourseRepo;
import com.odc.aws_learning.app.entity.UserProgress;
import com.odc.aws_learning.app.entity.Quiz;
import com.odc.aws_learning.app.entity.DetailsCourse;
import com.odc.aws_learning.app.constante.Enumeration;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/learn")
@RequiredArgsConstructor
public class LearnerController {

    private final LearnerLessonService learnerLessonService;
    private final UserProgressRepository userProgressRepository;
    private final LessonRepository lessonRepository;
    private final QuizRepository quizRepository;
    private final UserQuizAttemptRepository userQuizAttemptRepository;
    private final DetailsCourseRepo detailsCourseRepo;

    @PostMapping("/{courseId}/lessons/{lessonId}/complete")
    public ResponseEntity<CResponse<?>> completeLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body(CResponse.error("User not authenticated"));
        }
        CResponse<?> response = learnerLessonService.completeLesson(courseId, lessonId, currentUser);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(400).body(response);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CResponse<?>> getCourseProgress(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body(CResponse.error("User not authenticated"));
        }
        CResponse<?> response = learnerLessonService.getCourseProgress(courseId, currentUser);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(404).body(response);
    }

    /**
     * Récupère l'activité récente de l'utilisateur (dernières leçons complétées)
     * GET /api/learn/recent-activity
     */
    @GetMapping("/recent-activity")
    public ResponseEntity<CResponse<?>> getRecentActivity(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "3") int limit) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body(CResponse.error("User not authenticated"));
        }
        
        try {
            // Récupérer les dernières leçons complétées, triées par date de complétion décroissante
            List<UserProgress> allProgress = userProgressRepository.findByUserId(currentUser.getId());
            List<UserProgress> recentProgress = allProgress.stream()
                .filter(up -> up.getCompletedAt() != null)
                .sorted((a, b) -> {
                    if (a.getCompletedAt() == null && b.getCompletedAt() == null) return 0;
                    if (a.getCompletedAt() == null) return 1;
                    if (b.getCompletedAt() == null) return -1;
                    return b.getCompletedAt().compareTo(a.getCompletedAt());
                })
                .limit(limit)
                .collect(Collectors.toList());

            List<Map<String, Object>> activityList = recentProgress.stream().map(up -> {
                Map<String, Object> activityMap = new HashMap<>();
                if (up.getLesson() != null && up.getLesson().getModule() != null && 
                    up.getLesson().getModule().getCourse() != null) {
                    activityMap.put("courseId", up.getLesson().getModule().getCourse().getId());
                    activityMap.put("courseTitle", up.getLesson().getModule().getCourse().getTitle());
                    activityMap.put("lessonTitle", up.getLesson().getTitle());
                    activityMap.put("completedAt", up.getCompletedAt());
                    // Calculer le pourcentage de progression du cours
                    Long courseId = up.getLesson().getModule().getCourse().getId();
                    long totalLessons = lessonRepository.countByModule_Course_Id(courseId);
                    long completedLessons = userProgressRepository
                        .findByUserIdAndLessonModuleCourseId(currentUser.getId(), courseId).size();
                    int progress = totalLessons > 0 ? (int) ((completedLessons * 100) / totalLessons) : 0;
                    activityMap.put("progress", progress);
                }
                return activityMap;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(CResponse.success(activityList, "Activité récente récupérée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(CResponse.error("Erreur lors de la récupération de l'activité récente: " + e.getMessage()));
        }
    }

    /**
     * Récupère la progression d'apprentissage de l'utilisateur par période
     * GET /api/learn/learning-progress?period=week|month|year
     */
    @GetMapping("/learning-progress")
    public ResponseEntity<CResponse<?>> getLearningProgress(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "week") String period) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body(CResponse.error("User not authenticated"));
        }
        
        try {
            List<UserProgress> allProgress = userProgressRepository.findByUserId(currentUser.getId());
            
            // Filtrer les progressions avec completedAt et lesson valides
            List<UserProgress> validProgress = allProgress.stream()
                .filter(up -> up.getCompletedAt() != null && up.getLesson() != null && up.getLesson().getDuration() != null)
                .collect(Collectors.toList());

            List<Map<String, Object>> progressData = new java.util.ArrayList<>();
            
            LocalDate now = LocalDate.now();
            LocalDateTime startDate;
            
            if ("week".equals(period)) {
                // Calculer les 7 derniers jours
                startDate = now.minusDays(6).atStartOfDay();
                Map<String, Double> dailyHours = new HashMap<>();
                
                // Initialiser tous les jours de la semaine à 0
                for (int i = 0; i < 7; i++) {
                    LocalDate date = now.minusDays(6 - i);
                    String dayKey = getDayAbbreviation(date);
                    dailyHours.put(dayKey, 0.0);
                }
                
                // Calculer les heures par jour
                for (UserProgress up : validProgress) {
                    LocalDateTime completedAt = up.getCompletedAt();
                    if (completedAt != null && !completedAt.isBefore(startDate)) {
                        LocalDate date = completedAt.toLocalDate();
                        String dayKey = getDayAbbreviation(date);
                        double hours = (up.getLesson().getDuration() != null ? up.getLesson().getDuration() : 0) / 60.0;
                        dailyHours.put(dayKey, dailyHours.getOrDefault(dayKey, 0.0) + hours);
                    }
                }
                
                // Créer les données pour le graphique
                for (int i = 0; i < 7; i++) {
                    LocalDate date = now.minusDays(6 - i);
                    String dayKey = getDayAbbreviation(date);
                    Map<String, Object> dayData = new HashMap<>();
                    dayData.put("day", dayKey);
                    dayData.put("hours", Math.round(dailyHours.getOrDefault(dayKey, 0.0) * 10.0) / 10.0);
                    dayData.put("goal", 3.0); // Objectif par défaut de 3h par jour
                    progressData.add(dayData);
                }
                
            } else if ("month".equals(period)) {
                // Calculer les 4 dernières semaines
                startDate = now.minusWeeks(3).atStartOfDay();
                Map<String, Double> weeklyHours = new HashMap<>();
                
                // Initialiser les 4 semaines
                for (int i = 0; i < 4; i++) {
                    String weekKey = "Sem " + (i + 1);
                    weeklyHours.put(weekKey, 0.0);
                }
                
                // Calculer les heures par semaine
                for (UserProgress up : validProgress) {
                    LocalDateTime completedAt = up.getCompletedAt();
                    if (completedAt != null && !completedAt.isBefore(startDate)) {
                        LocalDate date = completedAt.toLocalDate();
                        int weekNumber = getWeekNumber(date, now);
                        if (weekNumber >= 1 && weekNumber <= 4) {
                            String weekKey = "Sem " + weekNumber;
                            double hours = (up.getLesson().getDuration() != null ? up.getLesson().getDuration() : 0) / 60.0;
                            weeklyHours.put(weekKey, weeklyHours.getOrDefault(weekKey, 0.0) + hours);
                        }
                    }
                }
                
                // Créer les données pour le graphique
                for (int i = 0; i < 4; i++) {
                    String weekKey = "Sem " + (i + 1);
                    Map<String, Object> weekData = new HashMap<>();
                    weekData.put("week", weekKey);
                    weekData.put("hours", Math.round(weeklyHours.getOrDefault(weekKey, 0.0) * 10.0) / 10.0);
                    weekData.put("goal", 20.0); // Objectif par défaut de 20h par semaine
                    progressData.add(weekData);
                }
                
            } else if ("year".equals(period)) {
                // Calculer les 12 derniers mois
                startDate = now.minusMonths(11).atStartOfDay();
                Map<String, Double> monthlyHours = new HashMap<>();
                
                // Initialiser les 12 mois
                DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale.FRENCH);
                for (int i = 0; i < 12; i++) {
                    LocalDate monthDate = now.minusMonths(11 - i);
                    String monthKey = monthDate.format(monthFormatter);
                    monthlyHours.put(monthKey, 0.0);
                }
                
                // Calculer les heures par mois
                for (UserProgress up : validProgress) {
                    LocalDateTime completedAt = up.getCompletedAt();
                    if (completedAt != null && !completedAt.isBefore(startDate)) {
                        LocalDate date = completedAt.toLocalDate();
                        String monthKey = date.format(monthFormatter);
                        double hours = (up.getLesson().getDuration() != null ? up.getLesson().getDuration() : 0) / 60.0;
                        monthlyHours.put(monthKey, monthlyHours.getOrDefault(monthKey, 0.0) + hours);
                    }
                }
                
                // Créer les données pour le graphique
                for (int i = 0; i < 12; i++) {
                    LocalDate monthDate = now.minusMonths(11 - i);
                    String monthKey = monthDate.format(monthFormatter);
                    Map<String, Object> monthData = new HashMap<>();
                    monthData.put("month", monthKey);
                    monthData.put("hours", Math.round(monthlyHours.getOrDefault(monthKey, 0.0) * 10.0) / 10.0);
                    monthData.put("goal", 80.0); // Objectif par défaut de 80h par mois
                    progressData.add(monthData);
                }
            }

            return ResponseEntity.ok(CResponse.success(progressData, "Progression d'apprentissage récupérée avec succès"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(CResponse.error("Erreur lors de la récupération de la progression: " + e.getMessage()));
        }
    }

    /**
     * Obtient l'abréviation du jour de la semaine en français
     */
    private String getDayAbbreviation(LocalDate date) {
        String[] days = {"Dim", "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam"};
        return days[date.getDayOfWeek().getValue() % 7];
    }

    /**
     * Calcule le numéro de semaine relatif (1-4) pour une date donnée par rapport à maintenant
     */
    private int getWeekNumber(LocalDate date, LocalDate now) {
        LocalDate referenceWeekStart = now.minusWeeks(3);
        long daysDiff = java.time.temporal.ChronoUnit.DAYS.between(referenceWeekStart, date);
        int weekNumber = (int) (daysDiff / 7) + 1;
        return Math.max(1, Math.min(4, weekNumber)); // Limiter entre 1 et 4
    }

    /**
     * Récupère les échéances à venir pour l'utilisateur (quiz non complétés)
     * GET /api/learn/upcoming-deadlines
     */
    @GetMapping("/upcoming-deadlines")
    public ResponseEntity<CResponse<?>> getUpcomingDeadlines(
            @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body(CResponse.error("User not authenticated"));
        }
        
        try {
            // Récupérer les cours inscrits de l'utilisateur
            List<DetailsCourse> enrolledCourses = detailsCourseRepo.findByLearnerId(currentUser.getId())
                .stream()
                .filter(dc -> dc.isActivate() && !dc.isCompleted())
                .collect(Collectors.toList());

            List<Map<String, Object>> deadlines = new java.util.ArrayList<>();
            
            // Pour chaque cours inscrit, trouver les quiz non complétés avec succès
            for (DetailsCourse enrollment : enrolledCourses) {
                if (enrollment.getCourse() == null) continue;
                
                Long courseId = enrollment.getCourse().getId();
                List<Quiz> courseQuizzes = quizRepository.findByCourseId(courseId);
                
                for (Quiz quiz : courseQuizzes) {
                    if (quiz == null || !quiz.isActivate()) continue;
                    
                    // Vérifier si l'utilisateur a déjà réussi ce quiz
                    Optional<com.odc.aws_learning.app.entity.UserQuizAttempt> bestAttempt = 
                        userQuizAttemptRepository.findFirstByUserIdAndQuizIdOrderByScoreDesc(
                            currentUser.getId(), quiz.getId());
                    
                    boolean passed = bestAttempt.isPresent() && 
                        bestAttempt.get().getScore() != null && 
                        quiz.getScoreMinimum() != null &&
                        bestAttempt.get().getScore() >= quiz.getScoreMinimum();
                    
                    // Si le quiz n'a pas été réussi, l'ajouter aux échéances
                    if (!passed) {
                        Map<String, Object> deadline = new HashMap<>();
                        deadline.put("courseId", courseId);
                        deadline.put("course", enrollment.getCourse().getTitle());
                        deadline.put("task", quiz.getTitre() != null ? quiz.getTitre() : "Quiz");
                        deadline.put("taskType", "quiz");
                        deadline.put("quizId", quiz.getId());
                        
                        // Calculer la date d'échéance (par défaut: 7 jours après l'inscription)
                        LocalDateTime enrollmentDate = enrollment.getCreatedAt();
                        LocalDateTime dueDate = enrollmentDate != null ? 
                            enrollmentDate.plusDays(7) : LocalDateTime.now().plusDays(7);
                        
                        long daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), dueDate.toLocalDate());
                        String dueDateLabel;
                        if (daysUntilDue <= 0) {
                            dueDateLabel = "Aujourd'hui";
                            deadline.put("priority", "high");
                        } else if (daysUntilDue == 1) {
                            dueDateLabel = "Demain";
                            deadline.put("priority", "high");
                        } else if (daysUntilDue <= 3) {
                            dueDateLabel = "Dans " + daysUntilDue + " jours";
                            deadline.put("priority", "high");
                        } else if (daysUntilDue <= 7) {
                            dueDateLabel = "Dans " + daysUntilDue + " jours";
                            deadline.put("priority", "medium");
                        } else {
                            dueDateLabel = "Dans " + daysUntilDue + " jours";
                            deadline.put("priority", "low");
                        }
                        
                        deadline.put("dueDate", dueDateLabel);
                        deadline.put("dueDateTimestamp", dueDate);
                        deadlines.add(deadline);
                    }
                }
            }
            
            // Trier par priorité et date d'échéance
            deadlines.sort((a, b) -> {
                String priorityA = (String) a.get("priority");
                String priorityB = (String) b.get("priority");
                int priorityCompare = getPriorityOrder(priorityA).compareTo(getPriorityOrder(priorityB));
                if (priorityCompare != 0) return priorityCompare;
                
                LocalDateTime dateA = (LocalDateTime) a.get("dueDateTimestamp");
                LocalDateTime dateB = (LocalDateTime) b.get("dueDateTimestamp");
                if (dateA != null && dateB != null) {
                    return dateA.compareTo(dateB);
                }
                return 0;
            });
            
            // Limiter à 5 échéances
            deadlines = deadlines.stream().limit(5).collect(Collectors.toList());
            
            return ResponseEntity.ok(CResponse.success(deadlines, "Échéances récupérées avec succès"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(CResponse.error("Erreur lors de la récupération des échéances: " + e.getMessage()));
        }
    }

    /**
     * Récupère les prochaines étapes suggérées pour l'utilisateur
     * GET /api/learn/next-steps
     */
    @GetMapping("/next-steps")
    public ResponseEntity<CResponse<?>> getNextSteps(
            @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body(CResponse.error("User not authenticated"));
        }
        
        try {
            List<Map<String, Object>> nextSteps = new java.util.ArrayList<>();
            
            // Récupérer les cours inscrits non complétés
            List<DetailsCourse> enrolledCourses = detailsCourseRepo.findByLearnerId(currentUser.getId())
                .stream()
                .filter(dc -> dc.isActivate() && !dc.isCompleted())
                .collect(Collectors.toList());
            
            for (DetailsCourse enrollment : enrolledCourses) {
                if (enrollment.getCourse() == null) continue;
                
                Long courseId = enrollment.getCourse().getId();
                String courseTitle = enrollment.getCourse().getTitle();
                
                // Calculer la progression du cours
                long totalLessons = lessonRepository.countByModule_Course_Id(courseId);
                long completedLessons = userProgressRepository
                    .findByUserIdAndLessonModuleCourseId(currentUser.getId(), courseId).size();
                int progress = totalLessons > 0 ? (int) ((completedLessons * 100) / totalLessons) : 0;
                
                // Si le cours a une progression > 0 mais < 100%, suggérer de le compléter
                if (progress > 0 && progress < 100) {
                    Map<String, Object> step = new HashMap<>();
                    step.put("action", "Compléter le cours " + courseTitle);
                    step.put("progress", progress);
                    step.put("link", "/learn/" + courseId);
                    step.put("type", "course");
                    step.put("courseId", courseId);
                    nextSteps.add(step);
                }
                
                // Vérifier les quiz non complétés
                List<Quiz> courseQuizzes = quizRepository.findByCourseId(courseId);
                for (Quiz quiz : courseQuizzes) {
                    if (quiz == null || !quiz.isActivate()) continue;
                    
                    Optional<com.odc.aws_learning.app.entity.UserQuizAttempt> bestAttempt = 
                        userQuizAttemptRepository.findFirstByUserIdAndQuizIdOrderByScoreDesc(
                            currentUser.getId(), quiz.getId());
                    
                    boolean passed = bestAttempt.isPresent() && 
                        bestAttempt.get().getScore() != null && 
                        quiz.getScoreMinimum() != null &&
                        bestAttempt.get().getScore() >= quiz.getScoreMinimum();
                    
                    if (!passed) {
                        Map<String, Object> step = new HashMap<>();
                        step.put("action", "Passer le quiz " + (quiz.getTitre() != null ? quiz.getTitre() : "du module"));
                        step.put("progress", 0);
                        step.put("link", "/learn/" + courseId + "/quiz/" + quiz.getId());
                        step.put("type", "quiz");
                        step.put("courseId", courseId);
                        step.put("quizId", quiz.getId());
                        nextSteps.add(step);
                    }
                }
            }
            
            // Si pas assez de suggestions, ajouter une suggestion pour explorer les cours
            if (nextSteps.size() < 3) {
                Map<String, Object> exploreStep = new HashMap<>();
                exploreStep.put("action", "Explorer les nouveaux cours disponibles");
                exploreStep.put("progress", 0);
                exploreStep.put("link", "/courses");
                exploreStep.put("type", "explore");
                nextSteps.add(exploreStep);
            }
            
            // Limiter à 3 suggestions
            nextSteps = nextSteps.stream().limit(3).collect(Collectors.toList());
            
            return ResponseEntity.ok(CResponse.success(nextSteps, "Prochaines étapes récupérées avec succès"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(CResponse.error("Erreur lors de la récupération des prochaines étapes: " + e.getMessage()));
        }
    }

    /**
     * Obtient l'ordre de priorité pour le tri
     */
    private Integer getPriorityOrder(String priority) {
        if ("high".equals(priority)) return 1;
        if ("medium".equals(priority)) return 2;
        return 3; // low
    }
}
