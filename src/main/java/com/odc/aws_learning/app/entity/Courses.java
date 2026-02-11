package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.app.constante.CertificationMode;
import com.odc.aws_learning.app.constante.CourseLevel;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.app.constante.CourseStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference; // Added
import com.fasterxml.jackson.annotation.JsonBackReference; // Added


@Entity
@Table(name = "courses")
public class Courses extends BaseEntity {

    private String title;

    private String subtitle;

    @Lob
    private String description;

    private String imagePath;

    private Integer duration; // en secondes

    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    private String language;

    private Boolean bestseller = false;


    /**
     * Relation ManyToOne vers Catégorie (principale)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    @JsonBackReference
    private Categorie categorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference // Added to prevent recursion if User serializes Courses (User has no List<Courses> field for instructor yet)
    private User instructor;

    @ElementCollection
    @CollectionTable(name = "course_objectives", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "objective")
    private Set<String> objectives = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "course_features", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "feature")
    private Set<String> features = new HashSet<>();

    @OneToMany(
        mappedBy = "course",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonManagedReference // Added to manage serialization of Modules from Course
    private List<Module> modules = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added for DetailsCourse
    private List<DetailsCourse> detailsCourses = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added for Quiz
    private List<Quiz> quizzes = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added for Review
    private List<Review> reviews = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CourseStatus status = CourseStatus.BROUILLON;

    /** Mode de certification : examen (70 %) ou validation des labs par l'instructeur. */
    @Enumerated(EnumType.STRING)
    @Column(name = "certification_mode")
    private CertificationMode certificationMode = CertificationMode.BY_EXAM;

    @Lob
    private String rejectionReason;

    // NoArgsConstructor
    public Courses() {
    }

    // AllArgsConstructor
    public Courses(String title, String subtitle, String description, String imagePath, Integer duration, CourseLevel level, String language, Boolean bestseller, Categorie categorie, User instructor, Set<String> objectives, Set<String> features, List<Module> modules, List<DetailsCourse> detailsCourses, List<Quiz> quizzes, List<Review> reviews, CourseStatus status, String rejectionReason) {
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.imagePath = imagePath;
        this.duration = duration;
        this.level = level;
        this.language = language;
        this.bestseller = bestseller;
        this.categorie = categorie;
        this.instructor = instructor;
        this.objectives = objectives;
        this.features = features;
        this.modules = modules;
        this.detailsCourses = detailsCourses;
        this.quizzes = quizzes;
        this.reviews = reviews;
        this.status = status;
        this.rejectionReason = rejectionReason;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Integer getDuration() {
        return duration;
    }

    public CourseLevel getLevel() {
        return level;
    }

    public String getLanguage() {
        return language;
    }

    public Boolean getBestseller() {
        return bestseller;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public User getInstructor() {
        return instructor;
    }

    public Set<String> getObjectives() {
        return objectives;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<DetailsCourse> getDetailsCourses() {
        return detailsCourses;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public CertificationMode getCertificationMode() {
        return certificationMode != null ? certificationMode : CertificationMode.BY_EXAM;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setLevel(CourseLevel level) {
        this.level = level;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setBestseller(Boolean bestseller) {
        this.bestseller = bestseller;
    }



    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public void setObjectives(Set<String> objectives) {
        this.objectives = objectives;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public void setDetailsCourses(List<DetailsCourse> detailsCourses) {
        this.detailsCourses = detailsCourses;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public void setCertificationMode(CertificationMode certificationMode) {
        this.certificationMode = certificationMode;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Important for BaseEntity
        Courses courses = (Courses) o;
        return Objects.equals(title, courses.title) &&
               Objects.equals(subtitle, courses.subtitle) &&
               Objects.equals(description, courses.description) &&
               Objects.equals(imagePath, courses.imagePath) &&
               Objects.equals(duration, courses.duration) &&
               level == courses.level &&
               Objects.equals(language, courses.language) &&
               Objects.equals(bestseller, courses.bestseller) &&
               Objects.equals(categorie, courses.categorie) &&
               Objects.equals(instructor, courses.instructor) &&
               Objects.equals(modules, courses.modules) && // Added
               Objects.equals(detailsCourses, courses.detailsCourses) && // Added
               Objects.equals(quizzes, courses.quizzes) && // Added
               Objects.equals(reviews, courses.reviews) && // Added
               status == courses.status &&
               certificationMode == courses.certificationMode &&
               Objects.equals(rejectionReason, courses.rejectionReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, subtitle, description, imagePath, duration, level, language, bestseller, categorie, instructor, modules, detailsCourses, quizzes, reviews, status, certificationMode, rejectionReason);
    }

    @Override
    public String toString() {
        return "Courses{" +
               "title='" + title + '\'' +
               ", subtitle='" + subtitle + '\'' +
               ", description='" + description + '\'' +
               ", imagePath='" + imagePath + '\'' +
               ", duration=" + duration +
               ", level=" + level +
               ", language='" + language + '\'' +
               ", bestseller=" + bestseller +
               ", categorie=" + (categorie != null ? categorie.getId() : "null") +
               ", instructor=" + (instructor != null ? instructor.getId() : "null") +
               ", objectives=" + objectives.size() +
               ", features=" + features.size() +
               ", modules=" + (modules != null ? modules.size() : "null") +
               ", detailsCourses=" + (detailsCourses != null ? detailsCourses.size() : "null") +
               ", quizzes=" + (quizzes != null ? quizzes.size() : "null") +
               ", reviews=" + (reviews != null ? reviews.size() : "null") +
               ", status=" + status +
               ", certificationMode=" + certificationMode +
               ", rejectionReason='" + rejectionReason + '\'' +
               ", id=" + id +
               '}';
    }
}
