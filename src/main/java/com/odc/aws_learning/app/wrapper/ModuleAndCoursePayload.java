package com.odc.aws_learning.app.wrapper;

import com.odc.aws_learning.app.constante.CourseLevel;
import com.odc.aws_learning.app.entity.Module;

import java.util.List;

public class ModuleAndCoursePayload {
    public Long courseId;
    public List<Module> modules;
    public CourseLevel courseType;

    public CourseLevel getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseLevel courseType) {
        this.courseType = courseType;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
}
