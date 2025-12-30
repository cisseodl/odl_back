package com.odc.aws_learning.app.wrapper;

import com.odc.aws_learning.app.constante.COURSE_TYPE;
import com.odc.aws_learning.app.entity.Chapter;

import java.util.List;

public class ChapterAndCoursePayload {
    public Long courseId;
    public List<Chapter> chapters;
    public COURSE_TYPE courseType;

    public COURSE_TYPE getCourseType() {
        return courseType;
    }

    public void setCourseType(COURSE_TYPE courseType) {
        this.courseType = courseType;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
