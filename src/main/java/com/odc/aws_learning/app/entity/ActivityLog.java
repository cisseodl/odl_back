package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;

import javax.persistence.*;
import java.time.Instant; // Keep if Instant is strictly needed elsewhere, but not for audit timestamp
import java.time.LocalDateTime; // Use LocalDateTime for consistency with BaseEntity if needed
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonBackReference; // Added

@Entity
@Table(name = "activity_logs")
public class ActivityLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // Added to prevent recursion if User serializes ActivityLogs
    private User user;

    private String userName; // Denormalized for easier reporting
    private String action;
    private String resource; // e.g., "Course: AWS Basics", "Quiz: S3 Exam"
    // Removed redundant timestamp field - inherited from BaseEntity (as createdAt)
    @Lob
    private String details; // Additional details in JSON or plain text

    public ActivityLog(User user, String userName, String action, String resource, String details) {
        this.user = user;
        this.userName = userName;
        this.action = action;
        this.resource = resource;
        this.details = details;
        // this.timestamp = Instant.now(); // Inherited from BaseEntity (as createdAt)
    }

    public ActivityLog() {
        super();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    // Removed getTimestamp() - inherited from BaseEntity (as getCreatedAt())
    // Removed setTimestamp() - inherited from BaseEntity (as setCreatedAt())

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ActivityLog that = (ActivityLog) o;
        return Objects.equals(user, that.user) &&
               Objects.equals(userName, that.userName) &&
               Objects.equals(action, that.action) &&
               Objects.equals(resource, that.resource) &&
               // Objects.equals(timestamp, that.timestamp) && // Inherited from BaseEntity
               Objects.equals(details, that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, userName, action, resource, /* timestamp, */ details); // Removed timestamp
    }

    @Override
    public String toString() {
        return "ActivityLog{" +
               "user=" + (user != null ? user.getId() : "null") + // Avoid circular reference
               ", userName='" + userName + '\'' +
               ", action='" + action + '\'' +
               ", resource='" + resource + '\'' +
               ", timestamp=" + getCreatedAt() + // Use inherited method
               ", details='" + details + '\'' +
               '}';
    }
}
