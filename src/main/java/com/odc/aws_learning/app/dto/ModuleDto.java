package com.odc.aws_learning.app.dto;


import java.util.List;

public class ModuleDto {
    private Long id;
    private String title;
    private String duration; // Formatted as a string e.g., "1h 30m"
    private List<LessonDto> lessons;

    public ModuleDto() {
    }

    public ModuleDto(Long id, String title, String duration, List<LessonDto> lessons) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.lessons = lessons;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<LessonDto> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonDto> lessons) {
        this.lessons = lessons;
    }

    public static ModuleDtoBuilder builder() {
        return new ModuleDtoBuilder();
    }

    public static class ModuleDtoBuilder {
        private Long id;
        private String title;
        private String duration;
        private List<LessonDto> lessons;

        ModuleDtoBuilder() {
        }

        public ModuleDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ModuleDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ModuleDtoBuilder duration(String duration) {
            this.duration = duration;
            return this;
        }

        public ModuleDtoBuilder lessons(List<LessonDto> lessons) {
            this.lessons = lessons;
            return this;
        }

        public ModuleDto build() {
            return new ModuleDto(id, title, duration, lessons);
        }

        public String toString() {
            return "ModuleDto.ModuleDtoBuilder(id=" + this.id + ", title=" + this.title + ", duration=" + this.duration + ", lessons=" + this.lessons + ")";
        }
    }
}
