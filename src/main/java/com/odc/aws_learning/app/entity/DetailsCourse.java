package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.app.constante.Enumeration;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.base.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "details_course")
public class DetailsCourse extends BaseEntity {
    @ManyToOne
    private Courses course;
    @ManyToOne
    private User learner;
    private Enumeration.COURSE_STATUT courseStatut = Enumeration.COURSE_STATUT.Learning;

    public DetailsCourse() {
    }

    public DetailsCourse(Courses course, User learner, Enumeration.COURSE_STATUT courseStatut) {
        this.course = course;
        this.learner = learner;
        this.courseStatut = courseStatut;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public User getLearner() {
        return learner;
    }

    public void setLearner(User learner) {
        this.learner = learner;
    }

    public Enumeration.COURSE_STATUT getCourseStatut() {
        return courseStatut;
    }

    public void setCourseStatut(Enumeration.COURSE_STATUT courseStatut) {
        this.courseStatut = courseStatut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DetailsCourse that = (DetailsCourse) o;
        return courseStatut == that.courseStatut && Objects.equals(course, that.course) && Objects.equals(learner, that.learner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), course, learner, courseStatut);
    }
}
