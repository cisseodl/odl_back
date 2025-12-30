package com.odc.aws_learning.app.entity;


import com.odc.aws_learning.auth.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity()
@Table(name = "infotest")
@Data

public class InfoTest extends BaseEntity {
    @ManyToOne
    Evaluations evaluations;

}
