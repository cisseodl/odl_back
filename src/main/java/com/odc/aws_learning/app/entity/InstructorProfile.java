package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "instructor_profiles")
public class InstructorProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title; // e.g., "Senior AWS Architect", "Cloud Trainer"

    @Lob
    private String bio; // Biography or detailed description of the instructor

    // NoArgsConstructor
    public InstructorProfile() {
    }

    // AllArgsConstructor
    public InstructorProfile(User user, String title, String bio) {
        this.user = user;
        this.title = title;
        this.bio = bio;
    }

    // Getters
    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getBio() {
        return bio;
    }

    // Setters
    public void setUser(User user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Important for BaseEntity
        InstructorProfile that = (InstructorProfile) o;
        return Objects.equals(user, that.user) &&
               Objects.equals(title, that.title) &&
               Objects.equals(bio, that.bio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, title, bio);
    }
}