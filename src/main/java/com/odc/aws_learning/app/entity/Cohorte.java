package com.odc.aws_learning.app.entity;

// import com.fasterxml.jackson.databind.annotation.JsonDeserialize; // Keep for LocalDateTime
// import com.fasterxml.jackson.databind.annotation.JsonSerialize; // Keep for LocalDateTime
// import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer; // Keep for LocalDateTime
// import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer; // Keep for LocalDateTime
import com.odc.aws_learning.auth.base.entity.BaseEntity;
// import lombok.Data; // Lombok removed
// import lombok.EqualsAndHashCode; // Lombok removed

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects; // Added for equals/hashCode

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;


// @EqualsAndHashCode(callSuper = true) // Lombok removed
@Entity()
@Table(name = "cohorte")
// @Data // Lombok removed
public class Cohorte extends BaseEntity {

    private String nom;
    private String description;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") // Added for consistency with BaseEntity
    private LocalDateTime dateDebut;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") // Added for consistency with BaseEntity
    private LocalDateTime dateFin;

    public Cohorte() {
        super();
    }

    public Cohorte(String nom, String description, LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Cohorte cohorte = (Cohorte) o;
        return Objects.equals(nom, cohorte.nom) &&
               Objects.equals(description, cohorte.description) &&
               Objects.equals(dateDebut, cohorte.dateDebut) &&
               Objects.equals(dateFin, cohorte.dateFin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nom, description, dateDebut, dateFin);
    }

    @Override
    public String toString() {
        return "Cohorte{" +
               "nom='" + nom + '\'' +
               ", description='" + description + '\'' +
               ", dateDebut=" + dateDebut +
               ", dateFin=" + dateFin +
               ", id=" + id +
               '}';
    }
}
