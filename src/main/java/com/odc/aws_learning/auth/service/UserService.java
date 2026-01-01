package com.odc.aws_learning.auth.service;

import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();

    CResponse<?> getAll(int page, int size);

    public CResponse<?> checkUserByPhone(String phone);

    public CResponse<?> getUserById(Long id);
}
