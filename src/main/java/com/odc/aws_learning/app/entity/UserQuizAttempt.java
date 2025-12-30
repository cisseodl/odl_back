package com.odc.aws_learning.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.odc.aws_learning.auth.base.entity.BaseEntity;
import com.odc.aws_learning.auth.entities.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_quiz_attempt")
@Data
public class UserQuizAttempt extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = {"quizAttempts"}, allowSetters = true)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    @JsonIgnoreProperties(value = {"attempts"}, allowSetters = true)
    private Quiz quiz;
    
    private Double score; // Score obtenu (peut être un pourcentage ou un nombre de points)
    
    private Integer scoreTotal; // Score total possible
    
    @Column(name = "date_tentative", columnDefinition = "DATETIME")
    private LocalDateTime dateTentative;
    
    @PrePersist
    protected void onCreate() {
        if (dateTentative == null) {
            dateTentative = LocalDateTime.now();
        }
    }
}
