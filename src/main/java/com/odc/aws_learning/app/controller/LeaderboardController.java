package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.dto.LeaderboardEntry;
import com.odc.aws_learning.app.service.LeaderboardService;
import com.odc.aws_learning.auth.base.response.CResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/leaderboard")
@AllArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/overall")
    public ResponseEntity<CResponse<List<LeaderboardEntry>>> getOverallLeaderboard() {
        List<LeaderboardEntry> leaderboard = leaderboardService.getOverallLeaderboard();
        return ResponseEntity.ok(CResponse.success(leaderboard, "Classement général récupéré avec succès."));
    }

    // Placeholder for monthly leaderboard
    @GetMapping("/monthly")
    public ResponseEntity<CResponse<List<LeaderboardEntry>>> getMonthlyLeaderboard() {
        // TODO: Implement monthly leaderboard logic in LeaderboardService
        return ResponseEntity.ok(CResponse.success(Collections.emptyList(), "Endpoint non implémenté."));
    }

    // Placeholder for course leaderboard
    @GetMapping("/course/{courseId}")
    public ResponseEntity<CResponse<List<LeaderboardEntry>>> getCourseLeaderboard() {
        // TODO: Implement course leaderboard logic in LeaderboardService
        return ResponseEntity.ok(CResponse.success(Collections.emptyList(), "Endpoint non implémenté."));
    }

    // Placeholder for user details
    @GetMapping("/user/{userId}/details")
    public ResponseEntity<CResponse<Object>> getUserDetails() {
        // TODO: Implement user details logic in LeaderboardService
        return ResponseEntity.ok(CResponse.success(null, "Endpoint non implémenté."));
    }
}
