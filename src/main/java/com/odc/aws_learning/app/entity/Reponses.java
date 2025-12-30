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
@Table(name = "reponses")
@Data
public class Reponses extends BaseEntity {
    private String title;
    @Lob
    private String description;
    private String status;
    private String imagePath;
    @ManyToOne
    private Questions questions;


}
