package com.odc.aws_learning.auth.dao.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Added

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true) // Added
// @Data // Lombok removed
// @Builder // Lombok removed
// @NoArgsConstructor // Lombok removed
// @AllArgsConstructor // Lombok removed
public class SignUpRequest {
    private Long id;
    private String fullName;
//    private String lastName;
    private String email;
    private String password;
    private String phone;
    // private Boolean admin; // Removed
    // private Boolean activate; // Managed by User entity
    private String avatar;
    /** Si true, ne pas créer le profil Apprenant (création par admin : le profil sera créé via "Créer profil apprenant"). */
    private Boolean skipApprenantProfile;
    // private Role role; // Removed

    public SignUpRequest() {
    }

    public SignUpRequest(Long id, String fullName, String email, String password, String phone, String avatar) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getSkipApprenantProfile() {
        return skipApprenantProfile;
    }

    public void setSkipApprenantProfile(Boolean skipApprenantProfile) {
        this.skipApprenantProfile = skipApprenantProfile;
    }
}
