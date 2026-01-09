package com.odc.aws_learning.auth.dto;

import com.odc.aws_learning.auth.entities.Admin;
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
public class AdminWithUserDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private Boolean activate;
    
    // User data
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
    private Boolean userActivate;
    
    public static AdminWithUserDto fromAdmin(Admin admin) {
        if (admin == null) {
            return null;
        }
        
        User user = admin.getUser();
        if (user == null) {
            return AdminWithUserDto.builder()
                    .id(admin.getId())
                    .createdAt(admin.getCreatedAt())
                    .lastModifiedAt(admin.getLastModifiedAt())
                    .activate(admin.isActivate())
                    .build();
        }
        
        return AdminWithUserDto.builder()
                .id(admin.getId())
                .createdAt(admin.getCreatedAt())
                .lastModifiedAt(admin.getLastModifiedAt())
                .activate(admin.isActivate())
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .userActivate(user.getActivate())
                .build();
    }
}
