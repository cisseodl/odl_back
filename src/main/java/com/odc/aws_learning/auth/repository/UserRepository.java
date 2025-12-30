package com.odc.aws_learning.auth.repository;

import com.odc.aws_learning.auth.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Since email is unique, we'll find users by email
    Optional<User> findByEmail(String email);
    Page<User> findAllByActivateAndAdmin(boolean b, boolean admin, Pageable paging);
//    boolean existsByUsername(String username);
}
