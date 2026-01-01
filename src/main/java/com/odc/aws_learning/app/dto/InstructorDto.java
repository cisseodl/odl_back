package com.odc.aws_learning.app.dto;


public class InstructorDto {
    private Long id;
    private String name;
    private String avatar;
    private String title;
    private String bio;
    private long studentCount;
    private long courseCount;
    private double rating;

    public InstructorDto() {
    }

    public InstructorDto(Long id, String name, String avatar, String title, String bio, long studentCount, long courseCount, double rating) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.title = title;
        this.bio = bio;
        this.studentCount = studentCount;
        this.courseCount = courseCount;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public long getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(long studentCount) {
        this.studentCount = studentCount;
    }

    public long getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(long courseCount) {
        this.courseCount = courseCount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public static InstructorDtoBuilder builder() {
        return new InstructorDtoBuilder();
    }

    public static class InstructorDtoBuilder {
        private Long id;
        private String name;
        private String avatar;
        private String title;
        private String bio;
        private long studentCount;
        private long courseCount;
        private double rating;

        InstructorDtoBuilder() {
        }

        public InstructorDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public InstructorDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public InstructorDtoBuilder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public InstructorDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public InstructorDtoBuilder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public InstructorDtoBuilder studentCount(long studentCount) {
            this.studentCount = studentCount;
            return this;
        }

        public InstructorDtoBuilder courseCount(long courseCount) {
            this.courseCount = courseCount;
            return this;
        }

        public InstructorDtoBuilder rating(double rating) {
            this.rating = rating;
            return this;
        }

        public InstructorDto build() {
            return new InstructorDto(id, name, avatar, title, bio, studentCount, courseCount, rating);
        }

        public String toString() {
            return "InstructorDto.InstructorDtoBuilder(id=" + this.id + ", name=" + this.name + ", avatar=" + this.avatar + ", title=" + this.title + ", bio=" + this.bio + ", studentCount=" + this.studentCount + ", courseCount=" + this.courseCount + ", rating=" + this.rating + ")";
        }
    }
}
