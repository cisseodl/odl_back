package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.dto.ProfileDto;
import com.odc.aws_learning.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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
        CResponse<ProfileDto> response = (CResponse<ProfileDto>) userService.getProfileForUser(principal.getName());
        if (response.isSuccess() && response.getData() != null) {
            return ResponseEntity.ok(response.getData());
        }
        // Handle error cases or not found appropriately
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/me")
    public ResponseEntity<CResponse<?>> updateMyProfile(Principal principal, @RequestBody ProfileDto updatedProfileDto) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CResponse.error("Utilisateur non authentifié"));
        }
        CResponse<?> response = userService.updateProfileForUser(principal.getName(), updatedProfileDto);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/me/certificates")
    public ResponseEntity<CResponse<?>> getMyCertificates(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CResponse.error("Utilisateur non authentifié"));
        }
        CResponse<?> response = userService.getUserCertificates(principal.getName());
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
