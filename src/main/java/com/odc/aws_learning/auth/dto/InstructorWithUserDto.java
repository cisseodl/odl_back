package com.odc.aws_learning.auth.dto;

import com.odc.aws_learning.auth.entities.Instructor;
import com.odc.aws_learning.auth.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorWithUserDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private Boolean activate;
    private String biography;
    private String specialization;
    
    // User data
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
    private Boolean userActivate;
    
    public static InstructorWithUserDto fromInstructor(Instructor instructor) {
        if (instructor == null) {
            return null;
        }
        
        User user = instructor.getUser();
        if (user == null) {
            return InstructorWithUserDto.builder()
                    .id(instructor.getId())
                    .createdAt(instructor.getCreatedAt())
                    .lastModifiedAt(instructor.getLastModifiedAt())
                    .activate(instructor.isActivate())
                    .biography(instructor.getBiography())
                    .specialization(instructor.getSpecialization())
                    .build();
        }
        
        return InstructorWithUserDto.builder()
                .id(instructor.getId())
                .createdAt(instructor.getCreatedAt())
                .lastModifiedAt(instructor.getLastModifiedAt())
                .activate(instructor.isActivate())
                .biography(instructor.getBiography())
                .specialization(instructor.getSpecialization())
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .userActivate(user.getActivate())
                .build();
    }
}
