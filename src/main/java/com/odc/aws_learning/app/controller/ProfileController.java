package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.dto.ProfileDto;
import com.odc.aws_learning.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ProfileDto> getMyProfile(Principal principal) {
        // principal.getName() will give the username (email in our case)
        ProfileDto profileDto = userService.getProfileForUser(principal.getName());
        if (profileDto != null) {
            return ResponseEntity.ok(profileDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/me")
    public ResponseEntity<CResponse<?>> updateMyProfile(Principal principal, @RequestBody ProfileDto updatedProfileDto) {
        if (principal == null) {
            return ResponseEntity.status(401).body(CResponse.error("Utilisateur non authentifié"));
        }
        CResponse<?> response = userService.updateProfileForUser(principal.getName(), updatedProfileDto);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(500).body(response);
    }

    @GetMapping("/me/certificates")
    public ResponseEntity<CResponse<?>> getMyCertificates(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(CResponse.error("Utilisateur non authentifié"));
        }
        CResponse<?> response = userService.getUserCertificates(principal.getName());
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(404).body(response);
    }
}
