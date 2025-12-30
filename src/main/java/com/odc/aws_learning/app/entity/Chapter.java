package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity()
@Table(name = "chapter")
@Data
public class Chapter extends BaseEntity {
    private String title;
    @Lob
    private String description;
    private String pdfPath;
    private String chapterLink;
    @ManyToOne
    private Courses course;
}
