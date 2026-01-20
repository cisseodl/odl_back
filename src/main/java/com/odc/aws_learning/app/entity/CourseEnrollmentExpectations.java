package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;

import javax.persistence.*;

@Entity
@Table(name = "course_enrollment_expectations")
public class CourseEnrollmentExpectations extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "details_course_id", nullable = false)
    private DetailsCourse detailsCourse;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String expectations; // Les attentes de l'apprenant

    public CourseEnrollmentExpectations() {
    }

    public CourseEnrollmentExpectations(DetailsCourse detailsCourse, String expectations) {
        this.detailsCourse = detailsCourse;
        this.expectations = expectations;
    }

    public DetailsCourse getDetailsCourse() {
        return detailsCourse;
    }

    public void setDetailsCourse(DetailsCourse detailsCourse) {
        this.detailsCourse = detailsCourse;
    }

    public String getExpectations() {
        return expectations;
    }

    public void setExpectations(String expectations) {
        this.expectations = expectations;
    }
}
