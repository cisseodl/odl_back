package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.app.constante.Enumeration;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity()
@Table(name = "details_course")
@Data
public class DetailsCourse extends BaseEntity {
    @ManyToOne
    private Courses course;
    @ManyToOne
    private User learner;
    private Enumeration.COURSE_STATUT courseStatut = Enumeration.COURSE_STATUT.Learning;
}
