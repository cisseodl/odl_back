package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.Certificate;
import com.odc.aws_learning.app.dto.UserCertificationStats;
import com.odc.aws_learning.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    Optional<Certificate> findByUniqueCode(String uniqueCode);

    @Query("SELECT new com.odc.aws_learning.app.dto.UserCertificationStats(c.user.id, COUNT(c.id)) FROM Certificate c GROUP BY c.user.id")
    List<UserCertificationStats> findUserCertificationStats();

    long countByCourseIdIn(List<Long> courseIds); // Added for instructor analytics

    long countByUser(User user);
}
