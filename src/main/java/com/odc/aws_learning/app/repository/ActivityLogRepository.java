package com.odc.aws_learning.app.repository;

import com.odc.aws_learning.app.entity.ActivityLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // Added
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long>, JpaSpecificationExecutor<ActivityLog> {
    List<ActivityLog> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    List<ActivityLog> findByOrderByCreatedAtDesc(Pageable pageable);
    Optional<ActivityLog> findFirstByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT COUNT(DISTINCT al.user.id) FROM ActivityLog al WHERE al.createdAt > :date")
    long countDistinctUserByCreatedAtAfter(LocalDateTime date);

    @Query("SELECT COUNT(DISTINCT al.user.id) FROM ActivityLog al WHERE al.createdAt BETWEEN :start AND :end")
    long countDistinctUserByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
