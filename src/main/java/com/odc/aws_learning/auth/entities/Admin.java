package com.odc.aws_learning.auth.entities;

import com.odc.aws_learning.auth.base.entity.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference; // Added

@Entity
@Table(name = "admins")
public class Admin extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference // Added
    private User user;

    public Admin(User user) {
        this.user = user;
    }

    public Admin() {
        super();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Admin admin = (Admin) o;
        return Objects.equals(user, admin.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user);
    }

    @Override
    public String toString() {
        return "Admin{" +
               "user=" + (user != null ? user.getId() : "null") + // Avoid circular reference
               '}';
    }
}
