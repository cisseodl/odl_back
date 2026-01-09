package com.odc.aws_learning.app.dto;

import java.util.List;

public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone; // Added
    private Boolean admin; // Added (to indicate if Admin entity is linked)
    private Boolean activate; // Added
    private String avatar;
    private List<String> roles;
    private List<String> certificates; // Added

    public UserDto() {
    }

    public UserDto(Long id, String fullName, String email, String phone, Boolean admin, Boolean activate, String avatar, List<String> roles, List<String> certificates) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.admin = admin;
        this.activate = activate;
        this.avatar = avatar;
        this.roles = roles;
        this.certificates = certificates;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getActivate() {
        return activate;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<String> certificates) {
        this.certificates = certificates;
    }

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public static class UserDtoBuilder {
        private Long id;
        private String fullName;
        private String email;
        private String phone;
        private Boolean admin;
        private Boolean activate;
        private String avatar;
        private List<String> roles;
        private List<String> certificates;

        UserDtoBuilder() {
        }

        public UserDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserDtoBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public UserDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserDtoBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserDtoBuilder admin(Boolean admin) {
            this.admin = admin;
            return this;
        }

        public UserDtoBuilder activate(Boolean activate) {
            this.activate = activate;
            return this;
        }

        public UserDtoBuilder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public UserDtoBuilder roles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public UserDtoBuilder certificates(List<String> certificates) {
            this.certificates = certificates;
            return this;
        }

        public UserDto build() {
            return new UserDto(id, fullName, email, phone, admin, activate, avatar, roles, certificates);
        }

        public String toString() {
            return "UserDto.UserDtoBuilder(id=" + this.id + ", fullName=" + this.fullName + ", email=" + this.email + ", phone=" + this.phone + ", admin=" + this.admin + ", activate=" + this.activate + ", avatar=" + this.avatar + ", roles=" + this.roles + ", certificates=" + this.certificates + ")";
        }
    }
}
