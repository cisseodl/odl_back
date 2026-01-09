package com.odc.aws_learning.auth.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.odc.aws_learning.app.entity.ActivityLog; // Added
import com.odc.aws_learning.app.entity.Apprenant;
import com.odc.aws_learning.app.entity.Certificate;
import com.odc.aws_learning.app.entity.LabSession; // Added
import com.odc.aws_learning.app.entity.LearnerModule; // Added
import com.odc.aws_learning.app.entity.Notification; // Added
import com.odc.aws_learning.app.entity.Review; // Added
import com.odc.aws_learning.app.entity.UserProgress; // Added
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


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
    private Boolean activate;
    private String avatar;

    private Boolean emailNotificationsEnabled = true; // Default to true
    private Boolean smsNotificationsEnabled = false; // Default to false

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added
    private Apprenant apprenant;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added
    private Admin admin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added
    private Instructor instructor;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added
    private List<Certificate> certificates = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added for Notification
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added for ActivityLog
    private List<ActivityLog> activityLogs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added for UserProgress
    private List<UserProgress> userProgresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added for Review
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Added for LabSession
    private List<LabSession> labSessions = new ArrayList<>();

    @OneToMany(mappedBy = "learner", cascade = CascadeType.ALL, orphanRemoval = true) // learner, not user for LearnerModule
    @JsonManagedReference // Added for LearnerModule
    private List<LearnerModule> learnerModules = new ArrayList<>();

    public User() {
    }

    public User(Long id, String fullName, String email, String password, String phone, Boolean activate, String avatar, Boolean emailNotificationsEnabled, Boolean smsNotificationsEnabled, Apprenant apprenant, Admin admin, Instructor instructor, List<Certificate> certificates, List<Notification> notifications, List<ActivityLog> activityLogs, List<UserProgress> userProgresses, List<Review> reviews, List<LabSession> labSessions, List<LearnerModule> learnerModules) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.activate = activate;
        this.avatar = avatar;
        this.emailNotificationsEnabled = emailNotificationsEnabled;
        this.smsNotificationsEnabled = smsNotificationsEnabled;
        this.apprenant = apprenant;
        this.admin = admin;
        this.instructor = instructor;
        this.certificates = certificates;
        this.notifications = notifications;
        this.activityLogs = activityLogs;
        this.userProgresses = userProgresses;
        this.reviews = reviews;
        this.labSessions = labSessions;
        this.learnerModules = learnerModules;
    }

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

    public Boolean getEmailNotificationsEnabled() {
        return emailNotificationsEnabled;
    }

    public void setEmailNotificationsEnabled(Boolean emailNotificationsEnabled) {
        this.emailNotificationsEnabled = emailNotificationsEnabled;
    }

    public Boolean getSmsNotificationsEnabled() {
        return smsNotificationsEnabled;
    }

    public void setSmsNotificationsEnabled(Boolean smsNotificationsEnabled) {
        this.smsNotificationsEnabled = smsNotificationsEnabled;
    }

    public Apprenant getApprenant() {
        return apprenant;
    }

    public void setApprenant(Apprenant apprenant) {
        this.apprenant = apprenant;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<ActivityLog> getActivityLogs() {
        return activityLogs;
    }

    public void setActivityLogs(List<ActivityLog> activityLogs) {
        this.activityLogs = activityLogs;
    }

    public List<UserProgress> getUserProgresses() {
        return userProgresses;
    }

    public void setUserProgresses(List<UserProgress> userProgresses) {
        this.userProgresses = userProgresses;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<LabSession> getLabSessions() {
        return labSessions;
    }

    public void setLabSessions(List<LabSession> labSessions) {
        this.labSessions = labSessions;
    }

    public List<LearnerModule> getLearnerModules() {
        return learnerModules;
    }

    public void setLearnerModules(List<LearnerModule> learnerModules) {
        this.learnerModules = learnerModules;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = new ArrayList<>();
        if (this.admin != null) {
            roles.add("ROLE_ADMIN");
        }
        if (this.instructor != null) {
            roles.add("ROLE_INSTRUCTOR");
        }
        if (this.apprenant != null) {
            roles.add("ROLE_APPRENANT");
        }
        if (roles.isEmpty()) {
            roles.add("ROLE_USER");
        }
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
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
        return Objects.equals(id, user.id) &&
               Objects.equals(fullName, user.fullName) &&
               Objects.equals(email, user.email) &&
               Objects.equals(password, user.password) &&
               Objects.equals(phone, user.phone) &&
               Objects.equals(activate, user.activate) &&
               Objects.equals(avatar, user.avatar) &&
               Objects.equals(emailNotificationsEnabled, user.emailNotificationsEnabled) &&
               Objects.equals(smsNotificationsEnabled, user.smsNotificationsEnabled) &&
               Objects.equals(apprenant, user.apprenant) &&
               Objects.equals(admin, user.admin) &&
               Objects.equals(instructor, user.instructor) &&
               Objects.equals(certificates, user.certificates) &&
               Objects.equals(notifications, user.notifications) && // Added
               Objects.equals(activityLogs, user.activityLogs) && // Added
               Objects.equals(userProgresses, user.userProgresses) && // Added
               Objects.equals(reviews, user.reviews) && // Added
               Objects.equals(labSessions, user.labSessions) && // Added
               Objects.equals(learnerModules, user.learnerModules); // Added
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, email, password, phone, activate, avatar, emailNotificationsEnabled, smsNotificationsEnabled, apprenant, admin, instructor, certificates, notifications, activityLogs, userProgresses, reviews, labSessions, learnerModules); // Added
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", fullName='" + fullName + '\'' +
               ", email='" + email + '\'' +
               ", password='[PROTECTED]'" +
               ", phone='" + phone + '\'' +
               ", activate=" + activate +
               ", avatar='" + avatar + '\'' +
               ", emailNotificationsEnabled=" + emailNotificationsEnabled +
               ", smsNotificationsEnabled=" + smsNotificationsEnabled +
               ", apprenant=" + (apprenant != null ? apprenant.getId() : "null") + // Avoid circular reference
               ", admin=" + (admin != null ? admin.getId() : "null") + // Avoid circular reference
               ", instructor=" + (instructor != null ? instructor.getId() : "null") + // Avoid circular reference
               ", certificates=" + certificates.size() + // Avoid printing full certificates list
               ", notifications=" + notifications.size() + // Added
               ", activityLogs=" + activityLogs.size() + // Added
               ", userProgresses=" + userProgresses.size() + // Added
               ", reviews=" + reviews.size() + // Added
               ", labSessions=" + labSessions.size() + // Added
               ", learnerModules=" + learnerModules.size() + // Added
               '}';
    }
}
