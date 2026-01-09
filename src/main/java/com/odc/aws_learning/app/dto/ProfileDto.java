package com.odc.aws_learning.app.dto;

import java.util.List;
import java.util.Objects;

public class ProfileDto {
    private Long id;
    private String fullName;
    private String email;
    private String avatar;
    private List<String> enrolledCourses;
    private List<String> completedCourses;
    private List<String> certificates;

    public ProfileDto() {
    }

    public ProfileDto(Long id, String fullName, String email, String avatar, List<String> enrolledCourses, List<String> completedCourses, List<String> certificates) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.avatar = avatar;
        this.enrolledCourses = enrolledCourses;
        this.completedCourses = completedCourses;
        this.certificates = certificates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public List<String> getCompletedCourses() {
        return completedCourses;
    }

    public void setCompletedCourses(List<String> completedCourses) {
        this.completedCourses = completedCourses;
    }

    public List<String> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<String> certificates) {
        this.certificates = certificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileDto that = (ProfileDto) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(fullName, that.fullName) &&
               Objects.equals(email, that.email) &&
               Objects.equals(avatar, that.avatar) &&
               Objects.equals(enrolledCourses, that.enrolledCourses) &&
               Objects.equals(completedCourses, that.completedCourses) &&
               Objects.equals(certificates, that.certificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, email, avatar, enrolledCourses, completedCourses, certificates);
    }

    @Override
    public String toString() {
        return "ProfileDto{" +
               "id=" + id +
               ", fullName='" + fullName + '\'' +
               ", email='" + email + '\'' +
               ", avatar='" + avatar + '\'' +
               ", enrolledCourses=" + enrolledCourses +
               ", completedCourses=" + completedCourses +
               ", certificates=" + certificates +
               '}';
    }
}
