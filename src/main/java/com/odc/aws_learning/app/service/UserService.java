package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.dto.ProfileDto;
import com.odc.aws_learning.app.entity.Certificate;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ProfileDto getProfileForUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Assuming ProfileDto needs enrolledCourses and completedCourses as Strings for now
            // These would typically come from UserProgress or other tracking entities
            return ProfileDto.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .avatar(user.getAvatar())
                    .enrolledCourses(List.of("Course 1", "Course 2")) // Placeholder
                    .completedCourses(List.of("Course A")) // Placeholder
                    .certificates(user.getCertificates().stream()
                            .map(Certificate::getUniqueCode) // Or map to a CertificateDto
                            .collect(Collectors.toList()))
                    .build();
        }
        return null;
    }

    public CResponse<?> updateProfileForUser(String email, ProfileDto updatedProfileDto) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return CResponse.error("User not found");
        }
        User user = userOptional.get();
        user.setFullName(updatedProfileDto.getFullName());
        user.setAvatar(updatedProfileDto.getAvatar());
        // Do not allow email change here directly, handle separately if needed
        // Update other fields as necessary from updatedProfileDto
        userRepository.save(user);
        return CResponse.success(getProfileForUser(email), "Profile updated successfully");
    }

    public CResponse<?> getUserCertificates(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return CResponse.error("User not found");
        }
        User user = userOptional.get();
        // Return a list of Certificate DTOs or relevant info
        List<String> certificateUrls = user.getCertificates().stream()
                .map(Certificate::getCertificateUrl)
                .collect(Collectors.toList());
        return CResponse.success(certificateUrls, "User certificates fetched successfully");
    }
}