package com.odc.aws_learning.app.dto;


import java.util.List;

public class ProfileDto {
    private Long id;
    private String fullName;
    private String email;
    private String avatar;
    // Assuming we will create a simplified Course DTO for these lists
    // For now, using strings as placeholders
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

    public static ProfileDtoBuilder builder() {
        return new ProfileDtoBuilder();
    }

    public static class ProfileDtoBuilder {
        private Long id;
        private String fullName;
        private String email;
        private String avatar;
        private List<String> enrolledCourses;
        private List<String> completedCourses;
        private List<String> certificates;

        ProfileDtoBuilder() {
        }

        public ProfileDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProfileDtoBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public ProfileDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ProfileDtoBuilder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public ProfileDtoBuilder enrolledCourses(List<String> enrolledCourses) {
            this.enrolledCourses = enrolledCourses;
            return this;
        }

        public ProfileDtoBuilder completedCourses(List<String> completedCourses) {
            this.completedCourses = completedCourses;
            return this;
        }

        public ProfileDtoBuilder certificates(List<String> certificates) {
            this.certificates = certificates;
            return this;
        }

        public ProfileDto build() {
            return new ProfileDto(id, fullName, email, avatar, enrolledCourses, completedCourses, certificates);
        }

        public String toString() {
            return "ProfileDto.ProfileDtoBuilder(id=" + this.id + ", fullName=" + this.fullName + ", email=" + this.email + ", avatar=" + this.avatar + ", enrolledCourses=" + this.enrolledCourses + ", completedCourses=" + this.completedCourses + ", certificates=" + this.certificates + ")";
        }
    }
}
