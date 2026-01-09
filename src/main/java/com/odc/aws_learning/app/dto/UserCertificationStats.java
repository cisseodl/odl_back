package com.odc.aws_learning.app.dto;

// import lombok.AllArgsConstructor; // Removed
// import lombok.Data; // Removed
// import lombok.NoArgsConstructor; // Removed

import java.util.Objects; // Added for equals/hashCode

// @Data // Removed
// @AllArgsConstructor // Removed
// @NoArgsConstructor // Removed
public class UserCertificationStats {
    private Long userId;
    private Long certifications;

    public UserCertificationStats() {
    }

    public UserCertificationStats(Long userId, Long certifications) {
        this.userId = userId;
        this.certifications = certifications;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCertifications() {
        return certifications;
    }

    public void setCertifications(Long certifications) {
        this.certifications = certifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCertificationStats that = (UserCertificationStats) o;
        return Objects.equals(userId, that.userId) && Objects.equals(certifications, that.certifications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, certifications);
    }

    @Override
    public String toString() {
        return "UserCertificationStats{" +
               "userId=" + userId +
               ", certifications=" + certifications +
               '}';
    }
}
