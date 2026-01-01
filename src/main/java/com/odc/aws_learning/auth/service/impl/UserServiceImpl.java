package com.odc.aws_learning.auth.service.impl;

import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import com.odc.aws_learning.auth.service.UserService;
import com.odc.aws_learning.auth.base.response.CResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }


    @Override
    public CResponse<?> getAll(int page, int size) {
        try {
//            Sort defaultSort = Sort.by(Sort.Direction.DESC, "lastModifiedAt");
            Pageable paging = PageRequest.of(page, size);
            return CResponse.success(userRepository.findAllByActivateAndAdmin(true, true, paging), "Liste des utilisateurs");
        } catch (Exception e) {
            System.err.println(e);
            return CResponse.error("Erreur d'enregistrement, veuillez réessayer plus tard.");
        }
    }

    @Override
    public CResponse<?> checkUserByPhone(String phone) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(phone);
            if (userOptional.isPresent()) {
                return CResponse.success(userOptional.get());
            } else {
                return CResponse.error("Ce compte n'existe pas");
            }
        } catch (Exception e) {
            return CResponse.error("Erreur de création de compte");
        }
    }

    @Override
    public CResponse<?> getUserById(Long id) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                return CResponse.success(userOptional.get());
            } else {
                return CResponse.error("User not found with ID: " + id);
            }
        } catch (Exception e) {
            return CResponse.error("Error retrieving user: " + e.getMessage());
        }
    }

}
