package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.dto.LeaderboardEntry;
import com.odc.aws_learning.app.dto.UserCertificationStats;
import com.odc.aws_learning.app.dto.UserCourseCompletionStats;
import com.odc.aws_learning.app.repository.CertificateRepository;
import com.odc.aws_learning.app.repository.DetailsCourseRepo;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
// import lombok.AllArgsConstructor; // Removed
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
// @AllArgsConstructor // Removed
public class LeaderboardService {

    private final UserRepository userRepository;
    private final DetailsCourseRepo detailsCourseRepo;
    private final CertificateRepository certificateRepository;

    public LeaderboardService(UserRepository userRepository, DetailsCourseRepo detailsCourseRepo, CertificateRepository certificateRepository) {
        this.userRepository = userRepository;
        this.detailsCourseRepo = detailsCourseRepo;
        this.certificateRepository = certificateRepository;
    }

    public List<LeaderboardEntry> getOverallLeaderboard() {
        // 1. Fetch all stats
        Map<Long, Long> courseStats = detailsCourseRepo.findUserCourseCompletionStats().stream()
                .collect(Collectors.toMap(UserCourseCompletionStats::getUserId, UserCourseCompletionStats::getCompletedCourses));

        Map<Long, Long> certStats = certificateRepository.findUserCertificationStats().stream()
                .collect(Collectors.toMap(UserCertificationStats::getUserId, UserCertificationStats::getCertifications));

        // 2. Get all users who have some activity
        List<User> users = userRepository.findAllById(
                java.util.stream.Stream.concat(courseStats.keySet().stream(), certStats.keySet().stream())
                        .collect(Collectors.toSet())
        );

        // 3. Build Leaderboard Entries
        List<LeaderboardEntry> leaderboardEntries = users.stream().map(user -> {
            long coursesCompleted = courseStats.getOrDefault(user.getId(), 0L);
            long certifications = certStats.getOrDefault(user.getId(), 0L);
            return new LeaderboardEntry(
                    user.getId(),
                    user.getFullName(),
                    user.getAvatar(), // Assuming User entity has an avatar field
                    coursesCompleted,
                    certifications,
                    0, // Rank will be set later
                    null // Change is for monthly
            );
        }).collect(Collectors.toList());

        // 4. Sort the leaderboard
        leaderboardEntries.sort(
                Comparator.comparing(LeaderboardEntry::getCoursesCompleted).reversed()
                        .thenComparing(LeaderboardEntry::getCertifications).reversed()
        );

        // 5. Assign ranks
        AtomicInteger rank = new AtomicInteger(1);
        leaderboardEntries.forEach(entry -> entry.setRank(rank.getAndIncrement()));

        return leaderboardEntries;
    }
}
