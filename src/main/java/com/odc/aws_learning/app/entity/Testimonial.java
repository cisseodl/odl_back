package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "testimonials")
public class Testimonial extends BaseEntity {

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Testimonial() {
        super();
    }

    public Testimonial(String content, User user) {
        this.content = content;
        this.user = user;
    }

    // Getters
    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    // Setters
    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Testimonial that = (Testimonial) o;
        return Objects.equals(content, that.content) &&
               Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), content, user);
    }

    @Override
    public String toString() {
        return "Testimonial{"
               + "content='" + content + "'"
               + ", user=" + (user != null ? user.getId() : "null") + ","
               + "createdAt=" + getCreatedAt() + ","
               + "id=" + id + 
               '}';
    }
}
