package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.app.constante.LessonType;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "lessons")
@Data
public class Lesson extends BaseEntity {

    private String title;

    private Integer lessonOrder; // Pour éviter le mot-clé "order"

    @Enumerated(EnumType.STRING)
    private LessonType type;

    private String contentUrl;

    private Integer duration; // en secondes

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private Module module;
}