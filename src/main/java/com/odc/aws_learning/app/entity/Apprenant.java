package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // Ajouté

import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User; // Import User entity

import javax.persistence.*; // Import all persistence annotations
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference; // Added

@Entity()
@Table(name = "apprenants")
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

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference // Added
    private User user; // Lien direct vers l'entité User

    @Lob
    private String attentes; // Nouveau champ: attentes (type Text)
    private Boolean satisfaction; // Nouveau champ: satisfaction (type Boolean)

    // Champ temporaire pour la désérialisation de cohorteId depuis le JSON
    private transient Long cohorteId;

    public Apprenant(User user) {
        this.user = user;
        this.email = user.getEmail(); // Assuming email is consistent
    }

    public Apprenant() {
        super();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getNiveauEtude() {
        return niveauEtude;
    }

    public void setNiveauEtude(String niveauEtude) {
        this.niveauEtude = niveauEtude;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public Cohorte getCohorte() {
        return cohorte;
    }

    @JsonIgnore // Ignorer lors de la désérialisation pour éviter le conflit avec cohorteId
    public void setCohorte(Cohorte cohorte) {
        this.cohorte = cohorte;
    }

    // Getters et Setters pour cohorteId (utilisé pour la désérialisation)
    public Long getCohorteId() {
        return cohorteId;
    }

    public void setCohorteId(Long cohorteId) {
        this.cohorteId = cohorteId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAttentes() {
        return attentes;
    }

    public void setAttentes(String attentes) {
        this.attentes = attentes;
    }

    public Boolean getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Boolean satisfaction) {
        this.satisfaction = satisfaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Apprenant apprenant = (Apprenant) o;
        return Objects.equals(nom, apprenant.nom) &&
               Objects.equals(prenom, apprenant.prenom) &&
               Objects.equals(email, apprenant.email) &&
               Objects.equals(numero, apprenant.numero) &&
               Objects.equals(profession, apprenant.profession) &&
               Objects.equals(niveauEtude, apprenant.niveauEtude) &&
               Objects.equals(filiere, apprenant.filiere) &&
               Objects.equals(cohorte, apprenant.cohorte) &&
               Objects.equals(user, apprenant.user) &&
               Objects.equals(attentes, apprenant.attentes) &&
               Objects.equals(satisfaction, apprenant.satisfaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nom, prenom, email, numero, profession, niveauEtude, filiere, cohorte, user, attentes, satisfaction);
    }

    @Override
    public String toString() {
        return "Apprenant{" +
               "nom='" + nom + '\'' +
               ", prenom='" + prenom + '\'' +
               ", email='" + email + '\'' +
               ", numero='" + numero + '\'' +
               ", profession='" + profession + '\'' +
               ", niveauEtude='" + niveauEtude + '\'' +
               ", filiere='" + filiere + '\'' +
               ", cohorte=" + (cohorte != null ? cohorte.getId() : "null") + // Avoid circular reference
               ", user=" + (user != null ? user.getId() : "null") + // Avoid circular reference
               ", attentes='" + attentes + '\'' +
               ", satisfaction=" + satisfaction +
               '}';
    }
}
