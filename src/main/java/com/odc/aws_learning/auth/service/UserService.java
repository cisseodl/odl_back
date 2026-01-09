package com.odc.aws_learning.auth.service;

import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.app.dto.UserDto;
import com.odc.aws_learning.app.dto.ProfileDto; // Added
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.app.entity.Apprenant; // Added
import com.odc.aws_learning.auth.entities.Instructor; // Added
import com.odc.aws_learning.auth.repository.UserRepository;
import com.odc.aws_learning.app.repository.ApprenantRepository; // Added
import com.odc.aws_learning.auth.repository.InstructorRepository; // Added
import com.odc.aws_learning.app.repository.DetailsCourseRepo; // Added
import com.odc.aws_learning.app.entity.DetailsCourse; // Added
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ApprenantRepository apprenantRepository; // Injected
    private final InstructorRepository instructorRepository; // Injected
    private final DetailsCourseRepo detailsCourseRepo; // Injected

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public CResponse<?> getAll(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<User> pageUsers = userRepository.findAll(paging);
        List<UserDto> users = pageUsers.getContent().stream()
                .map(user -> {
                    List<String> roles = new ArrayList<>();
                    if (user.getAdmin() != null) roles.add("ADMIN");
                    if (user.getInstructor() != null) roles.add("INSTRUCTOR");
                    if (user.getApprenant() != null) roles.add("APPRENANT");
                    if (roles.isEmpty()) roles.add("USER"); // Default role

                    return UserDto.builder()
                            .id(user.getId())
                            .fullName(user.getFullName())
                            .email(user.getEmail())
                            .phone(user.getPhone())
                            .admin(user.getAdmin() != null) // Check if Admin entity is linked
                            .activate(user.getActivate())
                            .avatar(user.getAvatar())
                            .roles(roles)
                            .certificates(user.getCertificates().stream()
                                    .map(certificate -> certificate.getCertificateUrl() + ", Code:" + certificate.getUniqueCode())
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());

        return CResponse.success(users, "Liste des utilisateurs");
    }

    public CResponse<?> checkUserByPhone(String phone) {
        if (userRepository.findByPhone(phone).isPresent()){
            return CResponse.success("User Founded");
        }else {
            return CResponse.error("User not Founded");
        }
    }

    public CResponse<?> getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            List<String> roles = new ArrayList<>();
            if (user.getAdmin() != null) roles.add("ADMIN");
            if (user.getInstructor() != null) roles.add("INSTRUCTOR");
            if (user.getApprenant() != null) roles.add("APPRENANT");
            if (roles.isEmpty()) roles.add("USER"); // Default role

            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .admin(user.getAdmin() != null) // Check if Admin entity is linked
                    .activate(user.getActivate())
                    .avatar(user.getAvatar())
                    .roles(roles)
                    .certificates(user.getCertificates().stream()
                            .map(certificate -> certificate.getCertificateUrl() + ", Code:" + certificate.getUniqueCode())
                            .collect(Collectors.toList()))
                    .build();
            return CResponse.success(userDto, "Utilisateur par Id");
        }else {
            return CResponse.error("Utilisateur non trouvé");
        }
    }

    public CResponse<?> getUserCertificates(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Return a list of Certificate DTOs or relevant info
        List<String> certificateUrls = user.getCertificates().stream()
                .map(certificate -> certificate.getCertificateUrl() + ", Code:" + certificate.getUniqueCode())
                .collect(Collectors.toList());
        return CResponse.success(certificateUrls, "User certificates fetched successfully");
    }

    @Transactional
    public CResponse<ProfileDto> getProfileForUser(String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return CResponse.error("Utilisateur non trouvé avec l'email: " + userEmail);
        }
        User user = userOptional.get();

        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(user.getId());
        profileDto.setFullName(user.getFullName());
        profileDto.setEmail(user.getEmail());
        profileDto.setAvatar(user.getAvatar());

        // Populate enrolledCourses and completedCourses
        List<String> enrolledCourses = detailsCourseRepo.findByLearnerId(user.getId()).stream()
                .map(detailsCourse -> detailsCourse.getCourse().getTitle())
                .collect(Collectors.toList());
        profileDto.setEnrolledCourses(enrolledCourses);

        List<String> completedCourses = detailsCourseRepo.findByLearnerIdAndCourseStatut(user.getId(), com.odc.aws_learning.app.constante.Enumeration.COURSE_STATUT.Valide).stream()
                .map(detailsCourse -> detailsCourse.getCourse().getTitle())
                .collect(Collectors.toList());
        profileDto.setCompletedCourses(completedCourses);


        // Populate certificates
        profileDto.setCertificates(user.getCertificates().stream()
                .map(cert -> cert.getCertificateUrl() + " (Code: " + cert.getUniqueCode() + ")")
                .collect(Collectors.toList()));

        return CResponse.success(profileDto, "Profil utilisateur récupéré avec succès.");
    }

    @Transactional
    public CResponse<?> updateProfileForUser(String userEmail, ProfileDto profileDto) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return CResponse.error("Utilisateur non trouvé avec l'email: " + userEmail);
        }
        User user = userOptional.get();

        // Update basic user details
        if (profileDto.getFullName() != null) user.setFullName(profileDto.getFullName());
        if (profileDto.getEmail() != null) user.setEmail(profileDto.getEmail());
        if (profileDto.getAvatar() != null) user.setAvatar(profileDto.getAvatar());
        // Potentially update password if provided and handled securely

        // Update Apprenant details if user is an Apprenant
        if (user.getApprenant() != null) {
            Apprenant apprenant = user.getApprenant();
            // Assuming ProfileDto might contain specific Apprenant fields, e.g., attentes, satisfaction
            // For now, let's just update common fields or leave ApprenantService to handle its specifics
            // apprenant.setAttentes(profileDto.getAttentes());
            // apprenant.setSatisfaction(profileDto.getSatisfaction());
            // apprenantRepository.save(apprenant); // Need to save Apprenant too
        }

        // Update Instructor details if user is an Instructor
        if (user.getInstructor() != null) {
            Instructor instructor = user.getInstructor();
            // Assuming ProfileDto might contain specific Instructor fields, e.g., biography, specialization
            // instructor.setBiography(profileDto.getBiography());
            // instructor.setSpecialization(profileDto.getSpecialization());
            // instructorRepository.save(instructor); // Need to save Instructor too
        }

        userRepository.save(user);
        return CResponse.success(profileDto, "Profil utilisateur mis à jour avec succès.");
    }

    /**
     * Supprime un utilisateur et toutes ses entités liées en cascade (Admin, Instructor, Apprenant, Certificates, etc.)
     * Grâce à CascadeType.ALL et orphanRemoval = true dans les relations @OneToOne et @OneToMany,
     * la suppression de l'utilisateur supprimera automatiquement toutes les entités liées.
     */
    @Transactional
    public CResponse<?> deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return CResponse.error("Utilisateur non trouvé avec l'ID: " + userId);
        }
        
        User user = userOptional.get();
        
        // La suppression de l'utilisateur supprimera automatiquement en cascade :
        // - Admin (si présent)
        // - Instructor (si présent)
        // - Apprenant (si présent)
        // - Certificates
        // - Notifications
        // - ActivityLogs
        // - UserProgresses
        // - Reviews
        // - etc. (toutes les entités avec cascade = CascadeType.ALL et orphanRemoval = true)
        userRepository.delete(user);
        
        return CResponse.success(null, "Utilisateur et toutes ses données associées supprimés avec succès.");
    }
}
