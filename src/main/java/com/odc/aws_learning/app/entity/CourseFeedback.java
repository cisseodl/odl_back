package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;

import javax.persistence.*;

@Entity
@Table(name = "course_feedback")
public class CourseFeedback extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "details_course_id", nullable = false)
    private DetailsCourse detailsCourse;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String satisfaction; // Feedback de satisfaction du cours

    @Column(name = "exam_attempt_id")
    private Long examAttemptId; // Lien vers la tentative d'examen

    public CourseFeedback() {
    }

    public CourseFeedback(DetailsCourse detailsCourse, String satisfaction, Long examAttemptId) {
        this.detailsCourse = detailsCourse;
        this.satisfaction = satisfaction;
        this.examAttemptId = examAttemptId;
    }

    public DetailsCourse getDetailsCourse() {
        return detailsCourse;
    }

    public void setDetailsCourse(DetailsCourse detailsCourse) {
        this.detailsCourse = detailsCourse;
    }

    public String getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }

    public Long getExamAttemptId() {
        return examAttemptId;
    }

    public void setExamAttemptId(Long examAttemptId) {
        this.examAttemptId = examAttemptId;
    }
}
