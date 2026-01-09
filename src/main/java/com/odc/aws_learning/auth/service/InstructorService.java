package com.odc.aws_learning.auth.service;

import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.dto.InstructorWithUserDto;
import com.odc.aws_learning.auth.entities.Instructor;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.InstructorRepository;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
// import org.springframework.security.crypto.password.PasswordEncoder; // Removed
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository; // To manage User entity
    // private final PasswordEncoder passwordEncoder; // Removed

    @Transactional
    public CResponse<?> createInstructorAuthenticated(String userEmail, String biography, String specialization) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return CResponse.error("Utilisateur non trouvé avec l'email: " + userEmail);
        }
        User user = userOptional.get();

        if (instructorRepository.findByUserId(user.getId()).isPresent()) {
            return CResponse.error("Cet utilisateur est déjà un instructeur.");
        }

        Instructor instructor = new Instructor(user);
        instructor.setBiography(biography);
        instructor.setSpecialization(specialization);
        Instructor savedInstructor = instructorRepository.save(instructor);
        // Link instructor to user for bidirectional consistency
        user.setInstructor(savedInstructor);
        userRepository.save(user);

        return CResponse.success(savedInstructor, "Instructeur créé avec succès.");
    }

    @Transactional
    public CResponse<?> createInstructorForUser(Long userId, String biography, String specialization) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return CResponse.error("Utilisateur non trouvé avec l'ID: " + userId);
        }
        User user = userOptional.get();

        if (instructorRepository.findByUserId(user.getId()).isPresent()) {
            return CResponse.error("Cet utilisateur est déjà un instructeur.");
        }

        Instructor instructor = new Instructor(user);
        instructor.setBiography(biography);
        instructor.setSpecialization(specialization);
        Instructor savedInstructor = instructorRepository.save(instructor);
        // Link instructor to user for bidirectional consistency
        user.setInstructor(savedInstructor);
        userRepository.save(user);

        return CResponse.success(savedInstructor, "Instructeur créé avec succès.");
    }

    public CResponse<?> getAllInstructors() {
        // Utiliser JOIN FETCH pour charger la relation user de manière eager
        List<Instructor> instructors = instructorRepository.findAllWithUserJoinFetch();
        // Convertir en DTO pour inclure les données User dans la réponse JSON
        List<InstructorWithUserDto> instructorDtos = instructors.stream()
                .map(InstructorWithUserDto::fromInstructor)
                .collect(Collectors.toList());
        return CResponse.success(instructorDtos, "Liste des instructeurs.");
    }

    public CResponse<?> getInstructorById(Long id) {
        Optional<Instructor> instructorOptional = instructorRepository.findById(id);
        return instructorOptional.map(instructor -> CResponse.success(instructor, "Instructeur trouvé."))
                .orElse(CResponse.error("Instructeur non trouvé avec l'ID: " + id));
    }

    @Transactional
    public CResponse<?> updateInstructor(Long id, String biography, String specialization) {
        return instructorRepository.findById(id)
                .map(instructor -> {
                    if (biography != null) instructor.setBiography(biography);
                    if (specialization != null) instructor.setSpecialization(specialization);

                    return CResponse.success(instructorRepository.save(instructor), "Instructeur mis à jour avec succès.");
                }).orElse(CResponse.error("Instructeur non trouvé avec l'ID: " + id));
    }

    @Transactional
    public CResponse<?> deleteInstructor(Long id) {
        Optional<Instructor> instructorOptional = instructorRepository.findById(id);
        if (instructorOptional.isPresent()) {
            Instructor instructor = instructorOptional.get();
            User user = instructor.getUser();
            
            // Supprimer l'utilisateur, ce qui supprimera automatiquement l'Instructor en cascade
            // grâce à CascadeType.ALL et orphanRemoval = true dans la relation User -> Instructor
            userRepository.delete(user);
            
            return CResponse.success(null, "Instructeur et utilisateur associé supprimés avec succès.");
        }
        return CResponse.error("Instructeur non trouvé avec l'ID: " + id);
    }
}
