package com.odc.aws_learning.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
// import lombok.AllArgsConstructor; // Removed
// import lombok.Data; // Removed
// import lombok.NoArgsConstructor; // Removed

import java.util.Objects; // Added for equals/hashCode

// @Data // Removed
// @NoArgsConstructor // Removed
// @AllArgsConstructor // Removed
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaderboardEntry {
    private Long userId;
    private String userName;
    private String avatar;
    private long coursesCompleted;
    private long certifications;
    private int rank;
    private Integer change; // Can be null for overall leaderboard

    public LeaderboardEntry() {
    }

    public LeaderboardEntry(Long userId, String userName, String avatar, long coursesCompleted, long certifications, int rank, Integer change) {
        this.userId = userId;
        this.userName = userName;
        this.avatar = avatar;
        this.coursesCompleted = coursesCompleted;
        this.certifications = certifications;
        this.rank = rank;
        this.change = change;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getCoursesCompleted() {
        return coursesCompleted;
    }

    public void setCoursesCompleted(long coursesCompleted) {
        this.coursesCompleted = coursesCompleted;
    }

    public long getCertifications() {
        return certifications;
    }

    public void setCertifications(long certifications) {
        this.certifications = certifications;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Integer getChange() {
        return change;
    }

    public void setChange(Integer change) {
        this.change = change;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaderboardEntry that = (LeaderboardEntry) o;
        return coursesCompleted == that.coursesCompleted &&
               certifications == that.certifications &&
               rank == that.rank &&
               Objects.equals(userId, that.userId) &&
               Objects.equals(userName, that.userName) &&
               Objects.equals(avatar, that.avatar) &&
               Objects.equals(change, that.change);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, avatar, coursesCompleted, certifications, rank, change);
    }

    @Override
    public String toString() {
        return "LeaderboardEntry{" +
               "userId=" + userId +
               ", userName='" + userName + '\'' +
               ", avatar='" + avatar + '\'' +
               ", coursesCompleted=" + coursesCompleted +
               ", certifications=" + certifications +
               ", rank=" + rank +
               ", change=" + change +
               '}';
    }
}
