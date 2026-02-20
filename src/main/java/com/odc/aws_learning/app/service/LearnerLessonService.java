package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Lesson;
import com.odc.aws_learning.app.entity.DetailsCourse;
import com.odc.aws_learning.app.entity.UserProgress;
import com.odc.aws_learning.app.repository.LessonRepository;
import com.odc.aws_learning.app.dto.CourseProgressDto;
import com.odc.aws_learning.app.dto.LessonProgressDto;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Module;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.repository.DetailsCourseRepo;
import com.odc.aws_learning.app.repository.LabDefinitionRepository;
import com.odc.aws_learning.app.repository.ModuleRepository;
import com.odc.aws_learning.app.repository.QuizRepository;
import com.odc.aws_learning.app.repository.UserProgressRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearnerLessonService {

    private final UserProgressRepository userProgressRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final CoursesRepository coursesRepository;
    private final DetailsCourseRepo detailsCourseRepo;
    private final QuizRepository quizRepository;
    private final LabDefinitionRepository labDefinitionRepository;


    public CResponse<?> completeLesson(Long courseId, Long lessonId, User currentUser) {
        // Validation: user, lesson, and if lesson belongs to course
        Optional<User> userOptional = userRepository.findById(currentUser.getId());
        if (userOptional.isEmpty()) {
            return CResponse.error("User not found");
        }
        User user = userOptional.get();

        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonId);
        if (lessonOptional.isEmpty()) {
            return CResponse.error("Leçon introuvable.");
        }
        Lesson lesson = lessonOptional.get();
        if (lesson.getModule() == null) {
            return CResponse.error("Leçon sans module associé.");
        }
        Courses course = lesson.getModule().getCourse();
        if (course == null) {
            return CResponse.error("Module sans cours associé.");
        }

        // Check if the course is already completed for the user
        Optional<DetailsCourse> detailsCourseOptional = detailsCourseRepo.findByCourseIdAndLearnerId(course.getId(), user.getId());
        if (detailsCourseOptional.isPresent() && detailsCourseOptional.get().isCompleted()) {
            return CResponse.error("Ce cours est déjà marqué comme terminé et ne peut plus être modifié.");
        }

        // Check if the lesson actually belongs to the specified course
        if (!course.getId().equals(courseId)) {
            return CResponse.error("Cette leçon n'appartient pas au cours demandé (courseId=" + courseId + ", lesson.courseId=" + course.getId() + ").");
        }

        // If user has already completed this lesson, return success (idempotent) so the UI doesn't show 400
        Optional<UserProgress> existingProgress = userProgressRepository.findByUserAndLesson(user, lesson);
        if (existingProgress.isPresent()) {
            checkAndMarkCourseAsCompleted(user, course);
            return CResponse.success(existingProgress.get(), "Leçon déjà marquée comme terminée.");
        }

        UserProgress userProgress = new UserProgress();
        userProgress.setUser(user);
        userProgress.setLesson(lesson);
        userProgress.setCompletedAt(LocalDateTime.now());
        userProgressRepository.save(userProgress);

        // Check if the course is now completed
        checkAndMarkCourseAsCompleted(user, course);

        return CResponse.success(userProgress, "Lesson marked as completed successfully");
    }

    private void checkAndMarkCourseAsCompleted(User user, Courses course) {
        long totalLessonsInCourse = lessonRepository.countByModule_Course_Id(course.getId());
        long completedLessonsForUser = userProgressRepository.findByUserIdAndLessonModuleCourseId(user.getId(), course.getId()).size();

        if (totalLessonsInCourse > 0 && totalLessonsInCourse == completedLessonsForUser) {
            Optional<DetailsCourse> detailsCourseOptional = detailsCourseRepo.findByCourseIdAndLearnerId(course.getId(), user.getId());
            detailsCourseOptional.ifPresent(detailsCourse -> {
                if (!detailsCourse.isCompleted()) {
                    detailsCourse.setCompleted(true);
                    detailsCourseRepo.save(detailsCourse);
                }
            });
        }
    }

    public CResponse<?> getCourseProgress(Long courseId, User currentUser) {
        Optional<User> userOptional = userRepository.findById(currentUser.getId());
        if (userOptional.isEmpty()) {
            return CResponse.error("User not found");
        }
        Optional<Courses> courseOptional = coursesRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return CResponse.error("Course not found");
        }
        Courses course = courseOptional.get();

        List<Module> modules = moduleRepository.findByCourseId(courseId);
        int totalLessons = 0;
        for (Module module : modules) {
            totalLessons += lessonRepository.findByModuleId(module.getId()).size();
        }

        List<UserProgress> completedLessons = userProgressRepository.findByUserIdAndLessonModuleCourseId(currentUser.getId(), courseId);

        double progressPercentage = (totalLessons > 0) ? ((double) completedLessons.size() / totalLessons) * 100 : 0;

        // Quiz et labs par lessonId pour affichage apprenant (lab / TD / quiz associés à chaque leçon)
        Map<Long, List<Long>> quizIdsByLessonId = quizRepository.findByCourseIdAndActivateTrueWithLesson(courseId).stream()
                .filter(q -> q.getLesson() != null)
                .collect(Collectors.groupingBy(q -> q.getLesson().getId(),
                        Collectors.mapping(com.odc.aws_learning.app.entity.Quiz::getId, Collectors.toList())));
        Map<Long, List<Long>> labIdsByLessonId = labDefinitionRepository.findByCourseIdViaLesson(courseId).stream()
                .filter(l -> l.getLesson() != null)
                .collect(Collectors.groupingBy(l -> l.getLesson().getId(),
                        Collectors.mapping(com.odc.aws_learning.app.entity.LabDefinition::getId, Collectors.toList())));

        List<LessonProgressDto> lessonProgressDtos = new ArrayList<>();
        for (Module module : modules) {
            List<Lesson> lessonsInModule = lessonRepository.findByModuleId(module.getId());
            for (Lesson lesson : lessonsInModule) {
                boolean completed = completedLessons.stream().anyMatch(up -> up.getLesson().getId().equals(lesson.getId()));
                Long lid = lesson.getId();
                lessonProgressDtos.add(LessonProgressDto.builder()
                        .lessonId(lid)
                        .lessonTitle(lesson.getTitle())
                        .lessonType(lesson.getType())
                        .lessonDuration(lesson.getDuration())
                        .completed(completed)
                        .completedAt(completed ? completedLessons.stream()
                                .filter(up -> up.getLesson().getId().equals(lid))
                                .findFirst().get().getCompletedAt() : null)
                        .quizIds(quizIdsByLessonId.getOrDefault(lid, new ArrayList<>()))
                        .labIds(labIdsByLessonId.getOrDefault(lid, new ArrayList<>()))
                        .build());
            }
        }

        CourseProgressDto courseProgressDto = CourseProgressDto.builder()
                .courseId(courseId)
                .courseTitle(course.getTitle())
                .totalLessons(totalLessons)
                .completedLessons(completedLessons.size())
                .progressPercentage(progressPercentage)
                .lessons(lessonProgressDtos)
                .build();

        return CResponse.success(courseProgressDto, "Course progress fetched successfully");
    }
}
