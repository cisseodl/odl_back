package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Lesson;
import com.odc.aws_learning.app.entity.UserProgress;
import com.odc.aws_learning.app.repository.LessonRepository;
import com.odc.aws_learning.app.dto.CourseProgressDto;
import com.odc.aws_learning.app.dto.LessonProgressDto;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.Module;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.repository.ModuleRepository;
import com.odc.aws_learning.app.repository.UserProgressRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LearnerLessonService {

    private final UserProgressRepository userProgressRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final CoursesRepository coursesRepository;

    public CResponse<?> completeLesson(Long courseId, Long lessonId, User currentUser) {
        // Validation: user, lesson, and if lesson belongs to course
        Optional<User> userOptional = userRepository.findById(currentUser.getId());
        if (userOptional.isEmpty()) {
            return CResponse.error("User not found");
        }

        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonId);
        if (lessonOptional.isEmpty()) {
            return CResponse.error("Lesson not found");
        }
        Lesson lesson = lessonOptional.get();

        // Check if the lesson actually belongs to the specified course
        if (!lesson.getModule().getCourse().getId().equals(courseId)) {
            return CResponse.error("Lesson does not belong to the specified course");
        }

        // Check if user has already completed this lesson
        Optional<UserProgress> existingProgress = userProgressRepository.findByUserAndLesson(currentUser, lesson);
        if (existingProgress.isPresent()) {
            return CResponse.error("Lesson already completed by this user");
        }

        UserProgress userProgress = new UserProgress();
        userProgress.setUser(currentUser);
        userProgress.setLesson(lesson);
        userProgress.setCompletedAt(LocalDateTime.now());
        userProgressRepository.save(userProgress);

        return CResponse.success(userProgress, "Lesson marked as completed successfully");
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

        List<LessonProgressDto> lessonProgressDtos = new java.util.ArrayList<>();
        for (Module module : modules) {
            List<Lesson> lessonsInModule = lessonRepository.findByModuleId(module.getId());
            for (Lesson lesson : lessonsInModule) {
                boolean completed = completedLessons.stream().anyMatch(up -> up.getLesson().getId().equals(lesson.getId()));
                lessonProgressDtos.add(LessonProgressDto.builder()
                        .lessonId(lesson.getId())
                        .lessonTitle(lesson.getTitle())
                        .lessonType(lesson.getType())
                        .lessonDuration(lesson.getDuration())
                        .completed(completed)
                        .completedAt(completed ? completedLessons.stream()
                                .filter(up -> up.getLesson().getId().equals(lesson.getId()))
                                .findFirst().get().getCompletedAt() : null)
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
