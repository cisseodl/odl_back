package com.odc.aws_learning.auth.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.odc.aws_learning.app.entity.Apprenant;
import com.odc.aws_learning.app.entity.Certificate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;



@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String phone;
    private Boolean admin;
    private Boolean activate;
    private String avatar;

    @ManyToOne
    private Apprenant learner;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certificate> certificates = new ArrayList<>();

    // NoArgsConstructor
    public User() {
    }

    // AllArgsConstructor
    public User(Long id, String fullName, String email, String password, String phone, Boolean admin, Boolean activate, String avatar, Apprenant learner, Role role, List<Certificate> certificates) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.admin = admin;
        this.activate = activate;
        this.avatar = avatar;
        this.learner = learner;
        this.role = role;
        this.certificates = certificates;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getActivate() {
        return activate;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Apprenant getLearner() {
        return learner;
    }

    public void setLearner(Apprenant learner) {
        this.learner = learner;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activate != null && activate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(fullName, user.fullName) &&
               Objects.equals(email, user.email) && Objects.equals(password, user.password) &&
               Objects.equals(phone, user.phone) && Objects.equals(admin, user.admin) &&
               Objects.equals(activate, user.activate) && Objects.equals(avatar, user.avatar) &&
               Objects.equals(learner, user.learner) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, email, password, phone, admin, activate, avatar, learner, role);
    }
}
