package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
// import lombok.Data; // Removed
// import lombok.EqualsAndHashCode; // Removed

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.ManyToOne; // Added for relationships
import javax.persistence.JoinColumn; // Added for relationships
import java.util.Objects; // Added for equals/hashCode
import com.fasterxml.jackson.annotation.JsonBackReference; // Added

// @EqualsAndHashCode(callSuper = true) // Removed
@Entity()
@Table(name = "learner_module")
// @Data // Removed
public class LearnerModule extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "learner_id") // Assuming a foreign key to User
    @JsonBackReference // Added (assuming User has a List<LearnerModule>)
    private User learner;

    @ManyToOne
    @JoinColumn(name = "module_id") // Assuming a foreign key to Module
    @JsonBackReference // Added (assuming Module has a List<LearnerModule>)
    private Module module;

    public LearnerModule() {
        super();
    }

    public LearnerModule(User learner, Module module) {
        this.learner = learner;
        this.module = module;
    }

    public User getLearner() {
        return learner;
    }

    public void setLearner(User learner) {
        this.learner = learner;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LearnerModule that = (LearnerModule) o;
        return Objects.equals(learner, that.learner) &&
               Objects.equals(module, that.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), learner, module);
    }

    @Override
    public String toString() {
        return "LearnerModule{" +
               "learner=" + (learner != null ? learner.getId() : "null") +
               ", module=" + (module != null ? module.getId() : "null") +
               ", id=" + id +
               '}';
    }
}
