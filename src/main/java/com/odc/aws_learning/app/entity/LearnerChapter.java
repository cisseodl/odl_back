package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity()
@Table(name = "learner_chapter")
@Data
public class LearnerChapter extends BaseEntity {
    private User learner;
    private Chapter chapter;
}
