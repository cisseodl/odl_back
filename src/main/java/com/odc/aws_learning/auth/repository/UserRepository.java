package com.odc.aws_learning.auth.repository;

import com.odc.aws_learning.auth.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Page<User> findAllByActivateAndAdmin(boolean activate, boolean admin, Pageable pageable);
    Optional<User> findByPhone(String phone); // Added
}