package com.odc.aws_learning.app.dto;

// import lombok.AllArgsConstructor; // Removed
// import lombok.Data; // Removed
// import lombok.NoArgsConstructor; // Removed

import java.util.Objects; // Added for equals/hashCode

// @Data // Removed
// @AllArgsConstructor // Removed
// @NoArgsConstructor // Removed
public class UserCourseCompletionStats {
    private Long userId;
    private Long completedCourses;

    public UserCourseCompletionStats() {
    }

    public UserCourseCompletionStats(Long userId, Long completedCourses) {
        this.userId = userId;
        this.completedCourses = completedCourses;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCompletedCourses() {
        return completedCourses;
    }

    public void setCompletedCourses(Long completedCourses) {
        this.completedCourses = completedCourses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCourseCompletionStats that = (UserCourseCompletionStats) o;
        return Objects.equals(userId, that.userId) && Objects.equals(completedCourses, that.completedCourses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, completedCourses);
    }

    @Override
    public String toString() {
        return "UserCourseCompletionStats{" +
               "userId=" + userId +
               ", completedCourses=" + completedCourses +
               '}';
    }
}
