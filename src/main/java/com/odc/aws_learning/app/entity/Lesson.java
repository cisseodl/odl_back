package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.app.constante.LessonType;
import com.odc.aws_learning.auth.base.entity.BaseEntity;

import javax.persistence.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonManagedReference; // Added
import com.fasterxml.jackson.annotation.JsonBackReference; // Added
import java.util.ArrayList; // Added for default list initialization
import java.util.List;

@Entity
@Table(name = "lessons")
public class Lesson extends BaseEntity {

    private String title;

    private Integer lessonOrder;

    @Enumerated(EnumType.STRING)
    private LessonType type;

    private String contentUrl;

    private Integer duration;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "module_id", nullable = false)
    @JsonBackReference // Added (corresponds to Module.lessons @JsonManagedReference)
    private Module module;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added for UserProgress
    private List<UserProgress> userProgresses = new ArrayList<>();

    public Lesson() {
        super();
    }

    public Lesson(String title, Integer lessonOrder, LessonType type, String contentUrl, Integer duration, Module module, List<UserProgress> userProgresses) {
        this.title = title;
        this.lessonOrder = lessonOrder;
        this.type = type;
        this.contentUrl = contentUrl;
        this.duration = duration;
        this.module = module;
        this.userProgresses = userProgresses;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLessonOrder() {
        return lessonOrder;
    }

    public void setLessonOrder(Integer lessonOrder) {
        this.lessonOrder = lessonOrder;
    }

    public LessonType getType() {
        return type;
    }

    public void setType(LessonType type) {
        this.type = type;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public List<UserProgress> getUserProgresses() {
        return userProgresses;
    }

    public void setUserProgresses(List<UserProgress> userProgresses) {
        this.userProgresses = userProgresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(title, lesson.title) &&
               Objects.equals(lessonOrder, lesson.lessonOrder) &&
               type == lesson.type &&
               Objects.equals(contentUrl, lesson.contentUrl) &&
               Objects.equals(duration, lesson.duration) &&
               Objects.equals(module, lesson.module) &&
               Objects.equals(userProgresses, lesson.userProgresses); // Added
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, lessonOrder, type, contentUrl, duration, module, userProgresses); // Added
    }

    @Override
    public String toString() {
        return "Lesson{" +
               "title='" + title + '\'' +
               ", lessonOrder=" + lessonOrder +
               ", type=" + type +
               ", contentUrl='" + contentUrl + '\'' +
               ", duration=" + duration +
               ", module=" + (module != null ? module.getId() : "null") + // Avoid circular reference
               ", userProgresses=" + (userProgresses != null ? userProgresses.size() : "null") + // Added
               ", id=" + id +
               '}';
    }
}