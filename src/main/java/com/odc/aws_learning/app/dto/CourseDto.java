package com.odc.aws_learning.app.dto;

import com.odc.aws_learning.app.constante.CertificationMode;
import com.odc.aws_learning.app.constante.CourseLevel;

import java.util.List;
import java.util.Set;

public class CourseDto {
    private Long id;
    private String title;
    private String subtitle;
    private String description;
    private String imageUrl;
    private InstructorDto instructor;
    private String category;
    private CourseLevel level;
    private double rating;
    private int reviewCount;
    private String duration; // e.g., "23 heures"
    private String language;
    private String lastUpdated; // Formatted date
    private boolean bestseller;

    private Set<String> objectives;
    private Set<String> features;
    private List<ModuleDto> curriculum;
    private long enrolledCount;
    private com.odc.aws_learning.app.constante.CourseStatus status;
    private CertificationMode certificationMode;
    private String rejectionReason;

    public CourseDto() {
    }

    public CourseDto(Long id, String title, String subtitle, String description, String imageUrl, InstructorDto instructor, String category, CourseLevel level, double rating, int reviewCount, String duration, String language, String lastUpdated, boolean bestseller, Set<String> objectives, Set<String> features, List<ModuleDto> curriculum, long enrolledCount, com.odc.aws_learning.app.constante.CourseStatus status, CertificationMode certificationMode, String rejectionReason) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.imageUrl = imageUrl;
        this.instructor = instructor;
        this.category = category;
        this.level = level;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.duration = duration;
        this.language = language;
        this.lastUpdated = lastUpdated;
        this.bestseller = bestseller;
        this.objectives = objectives;
        this.features = features;
        this.curriculum = curriculum;
        this.enrolledCount = enrolledCount;
        this.status = status;
        this.certificationMode = certificationMode;
        this.rejectionReason = rejectionReason;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public com.odc.aws_learning.app.constante.CourseStatus getStatus() {
        return status;
    }

    public void setStatus(com.odc.aws_learning.app.constante.CourseStatus status) {
        this.status = status;
    }

    public CertificationMode getCertificationMode() {
        return certificationMode;
    }

    public void setCertificationMode(CertificationMode certificationMode) {
        this.certificationMode = certificationMode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public InstructorDto getInstructor() {
        return instructor;
    }

    public void setInstructor(InstructorDto instructor) {
        this.instructor = instructor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public CourseLevel getLevel() {
        return level;
    }

    public void setLevel(CourseLevel level) {
        this.level = level;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isBestseller() {
        return bestseller;
    }

    public void setBestseller(boolean bestseller) {
        this.bestseller = bestseller;
    }

    public Set<String> getObjectives() {
        return objectives;
    }

    public void setObjectives(Set<String> objectives) {
        this.objectives = objectives;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }

    public List<ModuleDto> getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(List<ModuleDto> curriculum) {
        this.curriculum = curriculum;
    }

    public long getEnrolledCount() {
        return enrolledCount;
    }

    public void setEnrolledCount(long enrolledCount) {
        this.enrolledCount = enrolledCount;
    }

    public static CourseDtoBuilder builder() {
        return new CourseDtoBuilder();
    }

    public static class CourseDtoBuilder {
        private Long id;
        private String title;
        private String subtitle;
        private String description;
        private String imageUrl;
        private InstructorDto instructor;
        private String category;
        private CourseLevel level;
        private double rating;
        private int reviewCount;
        private String duration;
        private String language;
        private String lastUpdated;
        private boolean bestseller;
        private Set<String> objectives;
        private Set<String> features;
        private List<ModuleDto> curriculum;
        private long enrolledCount;
        private com.odc.aws_learning.app.constante.CourseStatus status;
        private CertificationMode certificationMode;
        private String rejectionReason;

        CourseDtoBuilder() {
        }

        public CourseDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CourseDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public CourseDtoBuilder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public CourseDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CourseDtoBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public CourseDtoBuilder instructor(InstructorDto instructor) {
            this.instructor = instructor;
            return this;
        }

        public CourseDtoBuilder category(String category) {
            this.category = category;
            return this;
        }

        public CourseDtoBuilder level(CourseLevel level) {
            this.level = level;
            return this;
        }

        public CourseDtoBuilder rating(double rating) {
            this.rating = rating;
            return this;
        }

        public CourseDtoBuilder reviewCount(int reviewCount) {
            this.reviewCount = reviewCount;
            return this;
        }

        public CourseDtoBuilder duration(String duration) {
            this.duration = duration;
            return this;
        }

        public CourseDtoBuilder language(String language) {
            this.language = language;
            return this;
        }

        public CourseDtoBuilder lastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public CourseDtoBuilder bestseller(boolean bestseller) {
            this.bestseller = bestseller;
            return this;
        }

        public CourseDtoBuilder objectives(Set<String> objectives) {
            this.objectives = objectives;
            return this;
        }

        public CourseDtoBuilder features(Set<String> features) {
            this.features = features;
            return this;
        }

        public CourseDtoBuilder curriculum(List<ModuleDto> curriculum) {
            this.curriculum = curriculum;
            return this;
        }

        public CourseDtoBuilder enrolledCount(long enrolledCount) {
            this.enrolledCount = enrolledCount;
            return this;
        }

        public CourseDtoBuilder status(com.odc.aws_learning.app.constante.CourseStatus status) {
            this.status = status;
            return this;
        }

        public CourseDtoBuilder certificationMode(CertificationMode certificationMode) {
            this.certificationMode = certificationMode;
            return this;
        }

        public CourseDtoBuilder rejectionReason(String rejectionReason) {
            this.rejectionReason = rejectionReason;
            return this;
        }

        public CourseDto build() {
            return new CourseDto(id, title, subtitle, description, imageUrl, instructor, category, level, rating, reviewCount, duration, language, lastUpdated, bestseller, objectives, features, curriculum, enrolledCount, status, certificationMode, rejectionReason);
        }

        public String toString() {
            return "CourseDto.CourseDtoBuilder(id=" + this.id + ", title=" + this.title + ", subtitle=" + this.subtitle + ", description=" + this.description + ", imageUrl=" + this.imageUrl + ", instructor=" + this.instructor + ", category=" + this.category + ", level=" + this.level + ", rating=" + this.rating + ", reviewCount=" + this.reviewCount + ", duration=" + this.duration + ", language=" + this.language + ", lastUpdated=" + this.lastUpdated + ", bestseller=" + this.bestseller + ", objectives=" + this.objectives + ", features=" + this.features + ", curriculum=" + this.curriculum + ", enrolledCount=" + this.enrolledCount + ", status=" + this.status + ", certificationMode=" + this.certificationMode + ", rejectionReason=" + this.rejectionReason + ")";
        }
    }
}