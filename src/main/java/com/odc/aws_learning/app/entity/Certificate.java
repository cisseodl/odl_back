package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference; // Added

@Entity
@Table(name = "certificates")
public class Certificate extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String uniqueCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference // Added
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Courses course;

    private Instant issuedAt;

    private String certificateUrl; // URL du PDF sur S3

    // NoArgsConstructor
    public Certificate() {
    }

    // AllArgsConstructor
    public Certificate(String uniqueCode, User user, Courses course, Instant issuedAt, String certificateUrl) {
        this.uniqueCode = uniqueCode;
        this.user = user;
        this.course = course;
        this.issuedAt = issuedAt;
        this.certificateUrl = certificateUrl;
    }

    // Getters
    public String getUniqueCode() {
        return uniqueCode;
    }

    public User getUser() {
        return user;
    }

    public Courses getCourse() {
        return course;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public String getCertificateUrl() {
        return certificateUrl;
    }

    // Setters
    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Important for BaseEntity
        Certificate that = (Certificate) o;
        return Objects.equals(uniqueCode, that.uniqueCode) &&
               Objects.equals(user, that.user) &&
               Objects.equals(course, that.course) &&
               Objects.equals(issuedAt, that.issuedAt) &&
               Objects.equals(certificateUrl, that.certificateUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uniqueCode, user, course, issuedAt, certificateUrl);
    }
}
