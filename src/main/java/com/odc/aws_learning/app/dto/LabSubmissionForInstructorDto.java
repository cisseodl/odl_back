package com.odc.aws_learning.app.dto;

import com.odc.aws_learning.app.entity.LabSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour afficher une soumission "Ma réalisation" (lab) côté instructeur.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabSubmissionForInstructorDto {
    private Long id;
    private Long labDefinitionId;
    private String labTitle;
    private Long courseId;
    private String courseTitle;
    private String lessonTitle;
    private Long userId;
    private String learnerName;
    private String learnerEmail;
    private String reportUrl;
    private LocalDateTime createdAt;

    public static LabSubmissionForInstructorDto from(LabSession session) {
        if (session == null) return null;
        LabSubmissionForInstructorDto dto = new LabSubmissionForInstructorDto();
        dto.setId(session.getId());
        dto.setLabDefinitionId(session.getLabDefinition() != null ? session.getLabDefinition().getId() : null);
        dto.setLabTitle(session.getLabDefinition() != null ? session.getLabDefinition().getTitle() : null);
        if (session.getLabDefinition() != null && session.getLabDefinition().getLesson() != null) {
            var lesson = session.getLabDefinition().getLesson();
            dto.setLessonTitle(lesson.getTitle());
            if (lesson.getModule() != null && lesson.getModule().getCourse() != null) {
                var course = lesson.getModule().getCourse();
                dto.setCourseId(course.getId());
                dto.setCourseTitle(course.getTitle());
            }
        }
        dto.setUserId(session.getUser() != null ? session.getUser().getId() : null);
        dto.setLearnerName(session.getUser() != null ? (session.getUser().getFullName() != null ? session.getUser().getFullName() : session.getUser().getEmail()) : null);
        dto.setLearnerEmail(session.getUser() != null ? session.getUser().getEmail() : null);
        dto.setReportUrl(session.getReportUrl());
        dto.setCreatedAt(session.getCreatedAt());
        return dto;
    }
}
