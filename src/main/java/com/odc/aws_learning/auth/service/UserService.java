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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Transactional(readOnly = true)
    public CResponse<?> getAll(int page, int size) {
        try {
            Pageable paging = PageRequest.of(page, size);
            
            // Utiliser findAll standard avec pagination, puis charger les relations si nécessaire
            Page<User> pageUsers = userRepository.findAll(paging);
            List<User> usersList = pageUsers.getContent();
            
            List<UserDto> users = usersList.stream()
                    .map(user -> {
                        try {
                            List<String> roles = new ArrayList<>();
                            
                            // Vérifier les relations de manière sécurisée
                            // Ces relations sont chargées en lazy, donc on doit les accéder dans la transaction
                            try {
                                if (user.getAdmin() != null) {
                                    roles.add("ADMIN");
                                }
                            } catch (Exception e) {
                                System.err.println("Erreur lors de l'accès à getAdmin() pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                            }
                            
                            try {
                                if (user.getInstructor() != null) {
                                    roles.add("INSTRUCTOR");
                                }
                            } catch (Exception e) {
                                System.err.println("Erreur lors de l'accès à getInstructor() pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                            }
                            
                            try {
                                if (user.getApprenant() != null) {
                                    roles.add("APPRENANT");
                                }
                            } catch (Exception e) {
                                System.err.println("Erreur lors de l'accès à getApprenant() pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                            }
                            
                            if (roles.isEmpty()) {
                                roles.add("USER"); // Default role
                            }

                            // Gérer les certificats de manière sécurisée
                            // Les certificats sont chargés en lazy, donc on doit les accéder dans la transaction
                            List<String> certificateList = new ArrayList<>();
                            try {
                                if (user.getCertificates() != null && !user.getCertificates().isEmpty()) {
                                    certificateList = user.getCertificates().stream()
                                            .filter(cert -> cert != null && cert.getCertificateUrl() != null)
                                            .map(certificate -> {
                                                String url = certificate.getCertificateUrl() != null ? certificate.getCertificateUrl() : "";
                                                String code = certificate.getUniqueCode() != null ? certificate.getUniqueCode() : "";
                                                return url + ", Code:" + code;
                                            })
                                            .collect(Collectors.toList());
                                }
                            } catch (Exception e) {
                                System.err.println("Erreur lors de l'accès aux certificats pour l'utilisateur " + user.getId() + ": " + e.getMessage());
                                e.printStackTrace();
                                // Continuer avec une liste vide de certificats
                            }

                            return UserDto.builder()
                                    .id(user.getId())
                                    .fullName(user.getFullName() != null ? user.getFullName() : "")
                                    .email(user.getEmail() != null ? user.getEmail() : "")
                                    .phone(user.getPhone())
                                    .admin(user.getAdmin() != null) // Check if Admin entity is linked
                                    .activate(user.getActivate() != null ? user.getActivate() : false)
                                    .avatar(user.getAvatar())
                                    .roles(roles)
                                    .certificates(certificateList)
                                    .build();
                        } catch (Exception e) {
                            System.err.println("Erreur lors de la conversion de l'utilisateur " + user.getId() + " en DTO: " + e.getMessage());
                            e.printStackTrace();
                            // Retourner un DTO minimal en cas d'erreur
                            return UserDto.builder()
                                    .id(user.getId())
                                    .fullName(user.getFullName() != null ? user.getFullName() : "")
                                    .email(user.getEmail() != null ? user.getEmail() : "")
                                    .phone(user.getPhone())
                                    .admin(false)
                                    .activate(user.getActivate() != null ? user.getActivate() : false)
                                    .avatar(user.getAvatar())
                                    .roles(new ArrayList<>())
                                    .certificates(new ArrayList<>())
                                    .build();
                        }
                    })
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());

            System.out.println("UserService.getAll() - Nombre d'utilisateurs convertis: " + users.size());
            
            // Retourner une structure paginée pour correspondre à ce que le frontend attend
            Map<String, Object> paginatedResponse = new HashMap<>();
            paginatedResponse.put("content", users);
            paginatedResponse.put("totalElements", pageUsers.getTotalElements());
            paginatedResponse.put("totalPages", pageUsers.getTotalPages());
            paginatedResponse.put("currentPage", page);
            paginatedResponse.put("size", size);
            
            return CResponse.success(paginatedResponse, "Liste des utilisateurs");
        } catch (Exception e) {
            System.err.println("Erreur dans getAll(): " + e.getMessage());
            System.err.println("Type d'exception: " + e.getClass().getName());
            e.printStackTrace();
            // Retourner une structure paginée vide plutôt qu'une erreur pour éviter de bloquer l'interface
            Map<String, Object> emptyResponse = new HashMap<>();
            emptyResponse.put("content", new ArrayList<>());
            emptyResponse.put("totalElements", 0L);
            emptyResponse.put("totalPages", 0);
            emptyResponse.put("currentPage", page);
            emptyResponse.put("size", size);
            return CResponse.success(emptyResponse, "Aucun utilisateur trouvé (erreur lors de la récupération)");
        }
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
     * Blacklister (désactiver) un utilisateur
     */
    @Transactional
    public CResponse<?> blacklistUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return CResponse.error("Utilisateur non trouvé avec l'ID: " + id);
        }
        
        User user = userOptional.get();
        user.setActivate(false);
        userRepository.save(user);
        
        return CResponse.success(null, "Utilisateur blacklisté (désactivé) avec succès.");
    }

    /**
     * Déblacklister (réactiver) un utilisateur
     */
    @Transactional
    public CResponse<?> unblacklistUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return CResponse.error("Utilisateur non trouvé avec l'ID: " + id);
        }
        
        User user = userOptional.get();
        user.setActivate(true);
        userRepository.save(user);
        
        return CResponse.success(null, "Utilisateur déblacklisté (réactivé) avec succès.");
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
