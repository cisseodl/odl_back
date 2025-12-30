package com.odc.aws_learning.app.entity;

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;


@EqualsAndHashCode(callSuper = true)
@Entity()
@Table(name = "categorie")
@Data
public class Categorie extends BaseEntity {
    private String title;
    @Lob
    private String description;
}
