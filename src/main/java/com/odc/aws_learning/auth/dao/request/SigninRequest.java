package com.odc.aws_learning.auth.dao.request;

// import lombok.AllArgsConstructor; // Removed
// import lombok.Builder; // Removed
// import lombok.Data; // Removed
// import lombok.NoArgsConstructor; // Removed

import java.util.Objects; // Added for equals/hashCode

// @Data // Removed
// @Builder // Removed
// @NoArgsConstructor // Removed
// @AllArgsConstructor // Removed
public class SigninRequest {
    private String email;
    private String password;

    public SigninRequest() {
    }

    public SigninRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SigninRequest that = (SigninRequest) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return "SigninRequest{" +
               "email='" + email + '\'' +
               ", password='[PROTECTED]'" +
               '}';
    }
}
