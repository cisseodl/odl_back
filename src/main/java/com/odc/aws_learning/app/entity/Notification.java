package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;

import javax.persistence.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonBackReference; // Added

@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // Added to prevent recursion if User serializes Notifications
    private User user;

    private String message;
    private String type; // e.g., "COURSE_UPDATE", "NEW_MESSAGE", "REMINDER"
    
    @Column(name = "is_read", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isRead;
    
    @Column(name = "is_archived", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isArchived;
    // Removed redundant createdAt field - inherited from BaseEntity
    private String link; // Added missing field declaration

    public Notification(User user, String message, String type, String link) {
        this.user = user;
        this.message = message;
        this.type = type;
        this.link = link;
        this.isRead = false;
        this.isArchived = false;
        // this.createdAt = LocalDateTime.now(); // Inherited from BaseEntity
    }

    public Notification() {
        super();
        // Initialiser les champs boolean pour éviter les erreurs SQL
        this.isRead = false;
        this.isArchived = false;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    // Removed redundant getCreatedAt() - inherited from BaseEntity
    // Removed redundant setCreatedAt() - inherited from BaseEntity

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Notification that = (Notification) o;
        return isRead == that.isRead &&
               isArchived == that.isArchived &&
               Objects.equals(user, that.user) &&
               Objects.equals(message, that.message) &&
               Objects.equals(type, that.type) &&
               // Objects.equals(createdAt, that.createdAt) && // Inherited from BaseEntity
               Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, message, type, isRead, isArchived, /* createdAt, */ link); // Removed createdAt
    }

    @Override
    public String toString() {
        return "Notification{" +
               "user=" + (user != null ? user.getId() : "null") +
               ", message='" + message + '\'' +
               ", type='" + type + '\'' +
               ", isRead=" + isRead +
               ", isArchived=" + isArchived +
               ", createdAt=" + getCreatedAt() + // Use inherited method
               ", link='" + link + '\'' +
               '}';
    }
}
