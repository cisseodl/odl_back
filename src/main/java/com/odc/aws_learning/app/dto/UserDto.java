package com.odc.aws_learning.app.dto;


import java.util.List;

public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private String avatar;
    private List<String> roles;

    public UserDto() {
    }

    public UserDto(Long id, String fullName, String email, String avatar, List<String> roles) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.avatar = avatar;
        this.roles = roles;
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

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public static class UserDtoBuilder {
        private Long id;
        private String fullName;
        private String email;
        private String avatar;
        private List<String> roles;

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

        public UserDtoBuilder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public UserDtoBuilder roles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public UserDto build() {
            return new UserDto(id, fullName, email, avatar, roles);
        }

        public String toString() {
            return "UserDto.UserDtoBuilder(id=" + this.id + ", fullName=" + this.fullName + ", email=" + this.email + ", avatar=" + this.avatar + ", roles=" + this.roles + ")";
        }
    }
}
