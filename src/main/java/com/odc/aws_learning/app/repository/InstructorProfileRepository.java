package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.InstructorProfile;
import com.odc.aws_learning.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorProfileRepository extends JpaRepository<InstructorProfile, Long> {
    Optional<InstructorProfile> findByUser(User user);
}
