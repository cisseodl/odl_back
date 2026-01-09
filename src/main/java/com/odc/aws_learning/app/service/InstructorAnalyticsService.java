package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.entity.DetailsCourse;
import com.odc.aws_learning.app.entity.UserQuizAttempt;
import com.odc.aws_learning.app.repository.*;
import com.odc.aws_learning.app.wrapper.InstructorCourseStatsDTO;
import com.odc.aws_learning.app.wrapper.LearnerProgressDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorAnalyticsService {

    private final CoursesRepository coursesRepository;
    private final DetailsCourseRepo detailsCourseRepo;
    private final UserProgressRepository userProgressRepository;
    private final LessonRepository lessonRepository;
    private final UserQuizAttemptRepository userQuizAttemptRepository;

    public InstructorCourseStatsDTO getCourseStats(Long courseId) {
        Courses course = coursesRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        List<DetailsCourse> enrollments = detailsCourseRepo.findAllByCourseId(courseId);
        long totalLessonsInCourse = lessonRepository.countByModule_Course_Id(courseId);

        List<LearnerProgressDTO> learnerStats = new ArrayList<>();
        double totalCompletionPercentage = 0;

        for (DetailsCourse enrollment : enrollments) {
            long completedLessons = userProgressRepository.countByUser_IdAndLesson_Module_Course_Id(enrollment.getLearner().getId(), courseId);
            double completionPercentage = (totalLessonsInCourse > 0) ? ((double) completedLessons / totalLessonsInCourse) * 100.0 : 0.0;
            totalCompletionPercentage += completionPercentage;

            Optional<UserQuizAttempt> bestAttempt = userQuizAttemptRepository.findFirstByUser_IdAndQuiz_Course_IdOrderByScoreDesc(enrollment.getLearner().getId(), courseId);
            Double bestScore = bestAttempt.map(UserQuizAttempt::getScore).orElse(null);

            learnerStats.add(LearnerProgressDTO.builder()
                    .learnerId(enrollment.getLearner().getId())
                    .learnerName(enrollment.getLearner().getFullName())
                    .completionPercentage(completionPercentage)
                    .bestQuizScore(bestScore)
                    .build());
        }

        double averageCompletionRate = (enrollments.isEmpty()) ? 0.0 : totalCompletionPercentage / enrollments.size();

        return InstructorCourseStatsDTO.builder()
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .totalEnrollments(enrollments.size())
                .averageCompletionRate(averageCompletionRate)
                .learnerStats(learnerStats)
                .build();
    }
}
