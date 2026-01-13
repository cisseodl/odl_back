package com.odc.aws_learning.app.service;

import com.odc.aws_learning.auth.dao.request.ApprenantCreateRequest;
import com.odc.aws_learning.app.entity.Apprenant;
import com.odc.aws_learning.app.entity.Cohorte;
import com.odc.aws_learning.app.repository.ApprenantRepository;
import com.odc.aws_learning.app.repository.CohorteRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
// import org.springframework.security.crypto.password.PasswordEncoder; // Removed
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.odc.aws_learning.app.service.SendEmailService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApprenantService {

    private final ApprenantRepository apprenantRepository;
    private final CohorteRepository cohorteRepository;
    private final UserRepository userRepository;
    private final SendEmailService sendEmailService;
    // private final PasswordEncoder passwordEncoder; // Removed
    
    @Value("${app.frontend.url:https://pi.smart-odc.com}")
    private String frontendUrl;

    @Transactional
    public CResponse<?> createApprenantAuthenticated(String emailFromJwt, ApprenantCreateRequest request) {
        User user;
        
        // Si userId est fourni, utiliser cet ID (pour permettre aux admins de créer un apprenant pour un autre utilisateur)
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID: " + request.getUserId()));
        } else if (request.getUserEmail() != null) {
            // Si userEmail est fourni, utiliser cet email
            user = userRepository.findByEmail(request.getUserEmail())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'email: " + request.getUserEmail()));
        } else {
            // Sinon, utiliser l'email du JWT (comportement par défaut)
            user = userRepository.findByEmail(emailFromJwt)
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        }

        if (apprenantRepository.findByUserId(user.getId()).isPresent()) {
            return CResponse.error("Cet utilisateur est déjà un apprenant.");
        }

        Apprenant apprenant = new Apprenant();
        apprenant.setActivate(request.getActivate() != null ? request.getActivate() : true);
        apprenant.setNom(request.getNom());
        apprenant.setPrenom(request.getPrenom());
        apprenant.setEmail(request.getEmail() != null ? request.getEmail() : user.getEmail()); // Utiliser l'email du DTO ou celui du User
        apprenant.setNumero(request.getNumero()); // Le numéro vient du DTO
        apprenant.setProfession(request.getProfession());
        apprenant.setNiveauEtude(request.getNiveauEtude());
        apprenant.setFiliere(request.getFiliere());
        apprenant.setAttentes(request.getAttentes());
        apprenant.setSatisfaction(request.getSatisfaction());
        apprenant.setUser(user);

        // cohorte si présente
        if (request.getCohorteId() != null) {
            Cohorte cohorte = cohorteRepository.findById(request.getCohorteId())
                    .orElseThrow(() -> new RuntimeException("Cohorte introuvable"));
            apprenant.setCohorte(cohorte);
        }

        Apprenant savedApprenant = apprenantRepository.save(apprenant);
        user.setApprenant(savedApprenant);
        userRepository.save(user);

        // Envoyer un email uniquement si l'apprenant est créé par un admin (userId fourni dans la requête)
        if (request.getUserId() != null) {
            try {
                String emailMessage = sendEmailService.mailTemplateApprenantCreated(
                    user.getFullName() != null ? user.getFullName() : user.getEmail(),
                    user.getEmail(),
                    frontendUrl
                );
                sendEmailService.sendEmailWithAttachment(
                    user.getEmail(),
                    emailMessage,
                    "Votre compte apprenant a été créé - Orange Digital Learning"
                );
            } catch (Exception e) {
                // Ne pas faire échouer la création si l'email échoue
                System.err.println("Erreur lors de l'envoi de l'email à l'apprenant: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return CResponse.success(savedApprenant, "Apprenant créé avec succès.");
    }

    public CResponse<?> getAllApprenants() {
        List<Apprenant> apprenants = apprenantRepository.findAll();
        return CResponse.success(apprenants, "Liste des apprenants.");
    }

    public CResponse<?> getApprenantById(Long id) {
        Optional<Apprenant> apprenantOptional = apprenantRepository.findById(id);
        return apprenantOptional.map(apprenant -> CResponse.success(apprenant, "Apprenant trouvé."))
                .orElse(CResponse.error("Apprenant non trouvé avec l'ID: " + id));
    }

    @Transactional
    public CResponse<?> updateApprenant(Long id, User userDetails, String nom, String prenom, String email, String numero, String profession, String niveauEtude, String filiere, Long cohorteId, String attentes, Boolean satisfaction) {
        return apprenantRepository.findById(id)
                .map(apprenant -> {
                    // Update user details if necessary (e.g., from a DTO)
                    User user = apprenant.getUser();
                    if (userDetails.getFullName() != null) user.setFullName(userDetails.getFullName());
                    if (userDetails.getEmail() != null) user.setEmail(userDetails.getEmail());
                    // ... update other user fields as needed ...
                    userRepository.save(user); // Save updated user details

                    if (nom != null) apprenant.setNom(nom);
                    if (prenom != null) apprenant.setPrenom(prenom);
                    if (email != null) apprenant.setEmail(email);
                    if (numero != null) apprenant.setNumero(numero);
                    if (profession != null) apprenant.setProfession(profession);
                    if (niveauEtude != null) apprenant.setNiveauEtude(niveauEtude);
                    if (filiere != null) apprenant.setFiliere(filiere);
                    if (attentes != null) apprenant.setAttentes(attentes);
                    if (satisfaction != null) apprenant.setSatisfaction(satisfaction);

                    if (cohorteId != null) {
                        cohorteRepository.findById(cohorteId).ifPresent(apprenant::setCohorte);
                    } else {
                        apprenant.setCohorte(null); // Explicitly unlink if cohorteId is null
                    }

                    return CResponse.success(apprenantRepository.save(apprenant), "Apprenant mis à jour avec succès.");
                }).orElse(CResponse.error("Apprenant non trouvé avec l'ID: " + id));
    }

    @Transactional
    public CResponse<?> deleteApprenant(Long id) {
        Optional<Apprenant> apprenantOptional = apprenantRepository.findById(id);
        if (apprenantOptional.isPresent()) {
            Apprenant apprenant = apprenantOptional.get();
            User user = apprenant.getUser();
            
            // Supprimer l'utilisateur, ce qui supprimera automatiquement l'Apprenant en cascade
            // grâce à CascadeType.ALL et orphanRemoval = true dans la relation User -> Apprenant
            userRepository.delete(user);
            
            return CResponse.success(null, "Apprenant et utilisateur associé supprimés avec succès.");
        }
        return CResponse.error("Apprenant non trouvé avec l'ID: " + id);
    }
    
    // Original methods to adapt/re-implement as needed
    public CResponse<?> getByCohorte(Long cohorteId, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Optional<Cohorte> cohorteOptional = cohorteRepository.findById(cohorteId);
        if (cohorteOptional.isEmpty()) {
            return CResponse.error("Cohorte non trouvée avec l'ID: " + cohorteId);
        }
        Page<Apprenant> apprenants = apprenantRepository.findAllByActivateAndCohorteId(true, cohorteId, paging);
        return CResponse.success(apprenants, "Les apprenants de " + cohorteOptional.get().getNom());
    }
}