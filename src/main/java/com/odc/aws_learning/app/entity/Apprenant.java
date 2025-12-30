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
@Table(name = "apprenants")
@Data
public class Apprenant extends BaseEntity{
    @Lob
    private String nom;
    private String prenom;
    private String email;
    private String numero;
    private String profession;
    private String niveauEtude;
    private String filiere;
    @ManyToOne
    private Cohorte cohorte;
}
