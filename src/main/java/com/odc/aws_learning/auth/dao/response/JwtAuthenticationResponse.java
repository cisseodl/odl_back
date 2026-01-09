package com.odc.aws_learning.auth.dao.response;

import com.odc.aws_learning.auth.entities.User;
// import lombok.AllArgsConstructor; // Removed
// import lombok.Builder; // Removed
// import lombok.Data; // Removed
// import lombok.NoArgsConstructor; // Removed

import java.util.Objects; // Added for equals/hashCode

// @Data // Removed
// @Builder // Removed
// @NoArgsConstructor // Removed
// @AllArgsConstructor // Removed
public class JwtAuthenticationResponse {
    private String token;
    private User user;

    public JwtAuthenticationResponse() {
    }

    public JwtAuthenticationResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtAuthenticationResponse that = (JwtAuthenticationResponse) o;
        return Objects.equals(token, that.token) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, user);
    }

    @Override
    public String toString() {
        return "JwtAuthenticationResponse{" +
               "token='" + token + '\'' +
               ", user=" + (user != null ? user.getId() : "null") + // Avoid circular reference
               '}';
    }
}
