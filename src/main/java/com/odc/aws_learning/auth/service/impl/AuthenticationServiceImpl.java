package com.odc.aws_learning.auth.service.impl;

import com.odc.aws_learning.app.constante.UploadLink;
import com.odc.aws_learning.app.entity.Apprenant;
import com.odc.aws_learning.app.entity.Cohorte;
import com.odc.aws_learning.app.repository.ApprenantRepository;
import com.odc.aws_learning.app.repository.CohorteRepository;
import com.odc.aws_learning.app.service.SendEmailService;
import com.odc.aws_learning.app.service.UploadFileService;
import com.odc.aws_learning.auth.dao.request.SignUpRequest;
import com.odc.aws_learning.auth.dao.request.SigninRequest;
import com.odc.aws_learning.auth.dao.request.UpdatePass;
import com.odc.aws_learning.auth.dao.response.JwtAuthenticationResponse;
import com.odc.aws_learning.auth.entities.Role;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import com.odc.aws_learning.auth.service.AuthenticationService;
import com.odc.aws_learning.auth.service.JwtService;
import com.odc.aws_learning.auth.base.response.CResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UploadFileService uploadFileService;
    private final SendEmailService sendEmailService;


    @Override
    public CResponse<JwtAuthenticationResponse> signup(SignUpRequest request, MultipartFile avatar) {
       try {
           // Check if user with this email already exists
           if (userRepository.findByEmail(request.getEmail()).isPresent()) {
               return CResponse.error("Cette adresse email est déjà utilisée pour un autre compte");
           }

           // save new user
           User user = new User();
           // Vérifier que les données essentielles sont présentes
           if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
               return CResponse.error("Le nom complet est requis.");
           }
           if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
               return CResponse.error("L'email est requis.");
           }
           if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
               return CResponse.error("Le mot de passe est requis.");
           }
           
           user.setFullName(request.getFullName().trim());
           user.setEmail(request.getEmail().trim());
           // Phone est optionnel, donc on peut le laisser null si non fourni
           if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
               user.setPhone(request.getPhone().trim());
           }
           // user.setAdmin(true); // Removed as role is determined by linked entity
           user.setActivate(true); // User is active by default upon signup
           user.setPassword(passwordEncoder.encode(request.getPassword()));
           if (avatar != null) {
               user.setAvatar(uploadFileService.uploadFile(avatar, UploadLink.DOWNLOAD_LINK + "/avatar"));
           }

           User savedUser = userRepository.save(user);
           // Flush pour s'assurer que l'utilisateur est bien sauvegardé avant de continuer
           userRepository.flush();
           
           // Recharger l'utilisateur depuis la base pour s'assurer qu'il a toutes les données
           savedUser = userRepository.findById(savedUser.getId())
                   .orElseThrow(() -> new RuntimeException("Erreur lors de la sauvegarde de l'utilisateur"));
           
           // Log pour déboguer - vérifier que les données sont bien sauvegardées
           System.out.println("User saved with ID: " + savedUser.getId());
           System.out.println("User fullName: " + savedUser.getFullName());
           System.out.println("User email: " + savedUser.getEmail());
           System.out.println("User phone: " + savedUser.getPhone());
           
           // Vérifier que fullName n'est pas null avant de continuer
           if (savedUser.getFullName() == null || savedUser.getFullName().trim().isEmpty()) {
               System.err.println("WARNING: User saved but fullName is null or empty! User ID: " + savedUser.getId());
           }
           
           var jwt = jwtService.generateToken(savedUser);
           JwtAuthenticationResponse authenticationResponse = new JwtAuthenticationResponse(jwt, savedUser);
           
           // Ne pas envoyer d'email lors de la création d'un utilisateur
           // L'email sera envoyé uniquement lors de la promotion à un rôle (admin, instructeur, apprenant)
           
           return CResponse.success(authenticationResponse, "Création de l'utilisateur réussie");
       } catch (Exception e) {
           System.err.println(e);
           e.printStackTrace(); // For debugging
           return CResponse.error("Erreur de création de compte");
       }
    }



    @Override
    public CResponse<?> updatePassword(UpdatePass updatePass) {
        try {
            Optional<User> applicationUser = userRepository.findByEmail(updatePass.getUsername());
            if (applicationUser.isPresent()) {
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(updatePass.getUsername(), updatePass.getPass1()));
                System.out.println("auth value: " + authentication);

                applicationUser.get().setPassword(passwordEncoder.encode(updatePass.getPass2()));
                userRepository.save(applicationUser.get());
                return CResponse.success(null, "Mot de passe modifié avec succès.");
            }
            return CResponse.error("Cet utilisateur n'existe pas");
        } catch (Exception e) {
            System.err.println(e);
            return CResponse.error("Modification du mot de passe échoué. Merci de vérifier si l'ancien mot de passe est correct");
        }
    }

    @Override
    public CResponse<JwtAuthenticationResponse> signin(SigninRequest request) {
        try {
            // Vérifier d'abord si l'utilisateur existe
            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isEmpty()) {
                System.err.println("DEBUG - Tentative de connexion avec email inexistant: " + request.getEmail());
                return CResponse.error("Email ou mot de passe incorrect.");
            }
            
            User user = userOptional.get();
            System.out.println("DEBUG - Tentative de connexion pour: " + request.getEmail() + " | Activate: " + user.getActivate());
            
            // Authentifier l'utilisateur avec Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            
            // Générer le token JWT
        var jwt = jwtService.generateToken(user);
            JwtAuthenticationResponse authenticationResponse = new JwtAuthenticationResponse(jwt, user);
            
            System.out.println("DEBUG - Connexion réussie pour: " + request.getEmail());
        return CResponse.success(authenticationResponse, "Authentification réussie");
            
        } catch (BadCredentialsException e) {
            System.err.println("DEBUG - BadCredentialsException pour: " + request.getEmail() + " | Message: " + e.getMessage());
            return CResponse.error("Email ou mot de passe incorrect.");
        } catch (AuthenticationException e) {
            System.err.println("DEBUG - AuthenticationException pour: " + request.getEmail() + " | Type: " + e.getClass().getSimpleName() + " | Message: " + e.getMessage());
            return CResponse.error("Erreur d'authentification. Veuillez vérifier vos identifiants.");
        } catch (Exception e) {
            System.err.println("DEBUG - Exception inattendue lors de la connexion: " + e.getClass().getSimpleName());
            e.printStackTrace();
            return CResponse.error("Une erreur s'est produite lors de la connexion. Veuillez réessayer.");
        }
    }

    String makePassword(int qoutas) {
        String passArray = "qwertyuiopasdfgghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM!@#$%^&*0123456789";
        int charactersLength = passArray.length();
        String result = "";
        for (int i = 0; i < qoutas; i++) {
            result += passArray.charAt((int) Math.floor(Math.random() * charactersLength));
        }
        return result;
    }

    public CResponse<?> forgetPass(String username) {
        try {
            Optional<User> applicationUser = userRepository.findByEmail(username);
            if (applicationUser.isPresent()) {
                String newPass = makePassword(6);
                applicationUser.get().setPassword(passwordEncoder.encode(newPass));
                userRepository.save(applicationUser.get());
                // Email désactivé pour le développement local
                // sendEmailService.sendEmailWithAttachment(applicationUser.get().getEmail(), newPass, "LOGIS NOUVEAU MOT DE PASSE");
                System.out.println("DEBUG - Nouveau mot de passe généré: " + newPass);
                return CResponse.success(applicationUser.get().getEmail(), "Nouveau mot de passe généré (vérifiez les logs pour le mot de passe).");
            }
            return CResponse.error("Cet utilisateur n'existe pas.");

        } catch (Exception e) {
            return CResponse.error("Erreur de rénitialisation de mot de passe.");
        }
    }
}
