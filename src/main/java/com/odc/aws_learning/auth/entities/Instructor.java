package com.odc.aws_learning.auth.entities;

import com.odc.aws_learning.auth.base.entity.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference; // Added

@Entity
@Table(name = "instructors")
public class Instructor extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference // Added
    private User user;

    @Lob
    private String biography; // Specific field for instructor
    private String specialization; // Specific field for instructor

    public Instructor(User user) {
        this.user = user;
    }

    public Instructor() {
        super();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Instructor that = (Instructor) o;
        return Objects.equals(user, that.user) &&
               Objects.equals(biography, that.biography) &&
               Objects.equals(specialization, that.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, biography, specialization);
    }

    @Override
    public String toString() {
        return "Instructor{" +
               "user=" + (user != null ? user.getId() : "null") + // Avoid circular reference
               ", biography='" + biography + '\'' +
               ", specialization='" + specialization + '\'' +
               '}';
    }
}
