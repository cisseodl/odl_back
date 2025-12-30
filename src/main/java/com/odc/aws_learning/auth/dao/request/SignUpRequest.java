package com.odc.aws_learning.auth.dao.request;

import com.odc.aws_learning.auth.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private Long id;
    private String fullName;
//    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Boolean admin;
    private Boolean activate;
    private String avatar;
    private Role role;
}
