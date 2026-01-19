package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.dto.LabDefinitionRequest;
import com.odc.aws_learning.app.entity.LabDefinition;
import com.odc.aws_learning.app.entity.LabSession;
import com.odc.aws_learning.app.entity.LabSessionStatus;
import com.odc.aws_learning.app.repository.LabDefinitionRepository;
import com.odc.aws_learning.app.repository.LabSessionRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
// import lombok.RequiredArgsConstructor; // Removed @Slf4j, so RequiredArgsConstructor might be okay or replaced.
// import lombok.extern.slf4j.Slf4j; // Removed
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger; // Added
import org.slf4j.LoggerFactory; // Added


/**
 * Service pour la gestion du cycle de vie des Labs (sessions pratiques).
 */
@Service
// @Slf4j // Removed
// @RequiredArgsConstructor // If @Slf4j is removed, we must provide explicit constructor
public class LabService {
    
    private static final Logger log = LoggerFactory.getLogger(LabService.class); // Manually added logger
    
    private final LabDefinitionRepository labDefinitionRepository;
    private final LabSessionRepository labSessionRepository;
    private final UserRepository userRepository;

    public LabService(LabDefinitionRepository labDefinitionRepository, LabSessionRepository labSessionRepository, UserRepository userRepository) {
        this.labDefinitionRepository = labDefinitionRepository;
        this.labSessionRepository = labSessionRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * Récupère toutes les définitions de labs disponibles.
     * @return Liste des labs disponibles
     */
    public CResponse<List<LabDefinition>> getAllLabs() {
        try {
            List<LabDefinition> labs = labDefinitionRepository.findAll();
            return CResponse.success(labs, "Liste des labs récupérée avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des labs: {}", e.getMessage(), e);
            return CResponse.error("Erreur lors de la récupération des labs: " + e.getMessage());
        }
    }
    
    /**
     * Récupère un lab par son ID.
     * @param id ID du lab
     * @return Lab trouvé
     */
    public CResponse<LabDefinition> getLabById(Long id) {
        try {
            Optional<LabDefinition> labOptional = labDefinitionRepository.findById(id);
            if (labOptional.isEmpty()) {
                return CResponse.error("Lab non trouvé avec l'ID: " + id);
            }
            return CResponse.success(labOptional.get(), "Lab récupéré avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du lab: {}", e.getMessage(), e);
            return CResponse.error("Erreur lors de la récupération du lab: " + e.getMessage());
        }
    }
    
    /**
     * Crée un nouveau lab.
     * @param request Données du lab à créer
     * @return Lab créé
     */
    @Transactional
    public CResponse<LabDefinition> createLab(LabDefinitionRequest request) {
        try {
            LabDefinition lab = new LabDefinition();
            lab.setTitle(request.getTitle());
            lab.setDescription(request.getDescription());
            lab.setUploadedFiles(request.getUploadedFiles());
            lab.setResourceLinks(request.getResourceLinks());
            lab.setInstructions(request.getInstructions());
            lab.setEstimatedDurationMinutes(request.getEstimatedDurationMinutes());
            lab.setMaxDurationMinutes(request.getMaxDurationMinutes());
            lab.setActivate(request.getActivate() != null ? request.getActivate() : true);
            
            LabDefinition savedLab = labDefinitionRepository.save(lab);
            log.info("Lab créé avec succès - ID: {}, Titre: {}", savedLab.getId(), savedLab.getTitle());
            return CResponse.success(savedLab, "Lab créé avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la création du lab: {}", e.getMessage(), e);
            return CResponse.error("Erreur lors de la création du lab: " + e.getMessage());
        }
    }
    
    /**
     * Met à jour un lab existant.
     * @param id ID du lab à mettre à jour
     * @param request Données du lab à mettre à jour
     * @return Lab mis à jour
     */
    @Transactional
    public CResponse<LabDefinition> updateLab(Long id, LabDefinitionRequest request) {
        try {
            Optional<LabDefinition> labOptional = labDefinitionRepository.findById(id);
            if (labOptional.isEmpty()) {
                return CResponse.error("Lab non trouvé avec l'ID: " + id);
            }
            
            LabDefinition lab = labOptional.get();
            lab.setTitle(request.getTitle());
            lab.setDescription(request.getDescription());
            lab.setUploadedFiles(request.getUploadedFiles());
            lab.setResourceLinks(request.getResourceLinks());
            lab.setInstructions(request.getInstructions());
            lab.setEstimatedDurationMinutes(request.getEstimatedDurationMinutes());
            lab.setMaxDurationMinutes(request.getMaxDurationMinutes());
            if (request.getActivate() != null) {
                lab.setActivate(request.getActivate());
            }
            
            LabDefinition updatedLab = labDefinitionRepository.save(lab);
            log.info("Lab mis à jour avec succès - ID: {}, Titre: {}", updatedLab.getId(), updatedLab.getTitle());
            return CResponse.success(updatedLab, "Lab mis à jour avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour du lab: {}", e.getMessage(), e);
            return CResponse.error("Erreur lors de la mise à jour du lab: " + e.getMessage());
        }
    }
    
    /**
     * Supprime un lab.
     * @param id ID du lab à supprimer
     * @return Réponse de succès
     */
    @Transactional
    public CResponse<Void> deleteLab(Long id) {
        try {
            Optional<LabDefinition> labOptional = labDefinitionRepository.findById(id);
            if (labOptional.isEmpty()) {
                return CResponse.error("Lab non trouvé avec l'ID: " + id);
            }
            
            // Vérifier s'il y a des sessions actives
            List<LabSession> activeSessions = labSessionRepository.findByLabDefinitionIdAndStatusIn(
                id,
                Arrays.asList(LabSessionStatus.STARTING, LabSessionStatus.RUNNING)
            );
            
            if (!activeSessions.isEmpty()) {
                return CResponse.error("Impossible de supprimer ce lab car il y a des sessions actives");
            }
            
            labDefinitionRepository.deleteById(id);
            log.info("Lab supprimé avec succès - ID: {}", id);
            return CResponse.success(null, "Lab supprimé avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la suppression du lab: {}", e.getMessage(), e);
            return CResponse.error("Erreur lors de la suppression du lab: " + e.getMessage());
        }
    }
    
    /**
     * Démarre une session de lab pour un utilisateur.
     * 
     * Logique :
     * 1. Vérifie si l'utilisateur a déjà une session active (RUNNING ou STARTING)
     * 2. Crée une nouvelle LabSession avec statut STARTING
     * 3. SIMULATION DE L'ORCHESTRATION : Génère une fausse URL et passe le statut à RUNNING
     * 4. Retourne la session créée
     * 
     * NOTE : Dans une implémentation réelle, c'est ici qu'on appellerait l'API Kubernetes
     * pour créer le conteneur/sandbox et obtenir l'URL réelle.
     * 
     * @param labDefId ID de la définition du lab
     * @param userEmail Email de l'utilisateur
     * @return Réponse contenant la session créée
     */
    @Transactional
    public CResponse<LabSession> startLab(Long labDefId, String userEmail) {
        try {
            // 1. Vérifier que le lab existe
            Optional<LabDefinition> labDefOptional = labDefinitionRepository.findById(labDefId);
            if (labDefOptional.isEmpty()) {
                return CResponse.error("Lab non trouvé avec l'ID: " + labDefId);
            }
            LabDefinition labDefinition = labDefOptional.get();
            
            // 2. Vérifier que l'utilisateur existe
            Optional<User> userOptional = userRepository.findByEmail(userEmail);
            if (userOptional.isEmpty()) {
                return CResponse.error("Utilisateur non trouvé avec l'email: " + userEmail);
            }
            User user = userOptional.get();
            
            // 3. Vérifier si l'utilisateur a déjà une session active pour ce lab
            List<LabSessionStatus> activeStatuses = Arrays.asList(
                    LabSessionStatus.STARTING, 
                    LabSessionStatus.RUNNING
            );
            Optional<LabSession> existingSession = labSessionRepository
                    .findFirstByUserIdAndLabDefinitionIdAndStatusIn(
                            user.getId(), 
                            labDefId, 
                            activeStatuses
                    );
            
            if (existingSession.isPresent()) {
                LabSession session = existingSession.get();
                return CResponse.error(
                        String.format(
                                "Vous avez déjà une session active pour ce lab. " +
                                "Session ID: %d, Statut: %s, URL: %s",
                                session.getId(),
                                session.getStatus(),
                                session.getContainerUrl()
                        )
                );
            }
            
            // 4. Créer une nouvelle session avec statut STARTING
            LabSession newSession = new LabSession();
            newSession.setUser(user);
            newSession.setLabDefinition(labDefinition);
            newSession.setStatus(LabSessionStatus.STARTING);
            newSession.setStartTime(LocalDateTime.now());
            newSession.setActivate(true);
            
            // 5. SIMULATION DE L'ORCHESTRATION
            // TODO: Dans une implémentation réelle, appeler ici l'API Kubernetes/Docker
            // pour créer le conteneur et obtenir l'URL réelle.
            // Exemple d'appel futur :
            // String containerUrl = kubernetesService.createLabContainer(
            //     labDefinition.getDockerImageName(),
            //     user.getId(),
            //     labDefId
            // );
            
            // Pour l'instant, génération d'une fausse URL
            String fakeContainerUrl = String.format(
                    "http://ec2-k8s-sandbox.aws.com/lab/%s",
                    UUID.randomUUID().toString()
            );
            
            newSession.setContainerUrl(fakeContainerUrl);
            
            // 6. Passer le statut à RUNNING (simulation d'un démarrage instantané)
            // Dans la réalité, on attendrait la confirmation de Kubernetes
            newSession.setStatus(LabSessionStatus.RUNNING);
            
            // 7. Sauvegarder la session
            LabSession savedSession = labSessionRepository.save(newSession);
            
            log.info("Session de lab démarrée - User: {}, Lab: {}, Session ID: {}, URL: {}",
                    userEmail, labDefinition.getTitle(), savedSession.getId(), fakeContainerUrl);
            
            return CResponse.success(savedSession, "Session de lab démarrée avec succès");
            
        } catch (Exception e) {
            log.error("Erreur lors du démarrage du lab: {}", e.getMessage(), e);
            return CResponse.error("Erreur lors du démarrage du lab: " + e.getMessage());
        }
    }
    
    /**
     * Arrête une session de lab.
     * Passe le statut à STOPPED et enregistre l'heure de fin.
     * 
     * NOTE : Dans une implémentation réelle, on appellerait ici l'API Kubernetes
     * pour arrêter/détruire le conteneur.
     * 
     * @param sessionId ID de la session à arrêter
     * @return Réponse contenant la session mise à jour
     */
    @Transactional
    public CResponse<LabSession> stopLab(Long sessionId) {
        try {
            Optional<LabSession> sessionOptional = labSessionRepository.findById(sessionId);
            if (sessionOptional.isEmpty()) {
                return CResponse.error("Session non trouvée avec l'ID: " + sessionId);
            }
            
            LabSession session = sessionOptional.get();
            
            // Vérifier que la session n'est pas déjà arrêtée ou soumise
            if (session.getStatus() == LabSessionStatus.STOPPED) {
                return CResponse.error("Cette session est déjà arrêtée");
            }
            
            if (session.getStatus() == LabSessionStatus.SUBMITTED) {
                return CResponse.error("Cette session a déjà été soumise et ne peut pas être arrêtée");
            }
            
            // TODO: Dans une implémentation réelle, appeler ici l'API Kubernetes
            // pour arrêter/détruire le conteneur
            // kubernetesService.stopLabContainer(session.getContainerUrl());
            
            // Mettre à jour le statut et l'heure de fin
            session.setStatus(LabSessionStatus.STOPPED);
            session.setEndTime(LocalDateTime.now());
            
            LabSession updatedSession = labSessionRepository.save(session);
            
            log.info("Session de lab arrêtée - Session ID: {}, User: {}",
                    sessionId, session.getUser().getEmail());
            
            return CResponse.success(updatedSession, "Session de lab arrêtée avec succès");
            
        } catch (Exception e) {
            log.error("Erreur lors de l'arrêt du lab: {}", e.getMessage(), e);
            return CResponse.error("Erreur lors de l'arrêt du lab: " + e.getMessage());
        }
    }
    
    /**
     * Permet à l'étudiant de soumettre son travail.
     * Passe le statut à SUBMITTED et enregistre l'URL du rapport.
     * 
     * @param sessionId ID de la session à soumettre
     * @param reportUrl URL du rapport soumis par l'étudiant (optionnel)
     * @return Réponse contenant la session mise à jour
     */
    @Transactional
    public CResponse<LabSession> submitLab(Long sessionId, String reportUrl) {
        try {
            Optional<LabSession> sessionOptional = labSessionRepository.findById(sessionId);
            if (sessionOptional.isEmpty()) {
                return CResponse.error("Session non trouvée avec l'ID: " + sessionId);
            }
            
            LabSession session = sessionOptional.get();
            
            // Vérifier que la session est en cours (RUNNING)
            if (session.getStatus() != LabSessionStatus.RUNNING) {
                return CResponse.error(
                        String.format(
                                "Impossible de soumettre cette session. Statut actuel: %s. " +
                                "Seules les sessions RUNNING peuvent être soumises.",
                                session.getStatus()
                        )
                );
            }
            
            // Mettre à jour le statut et l'URL du rapport
            session.setStatus(LabSessionStatus.SUBMITTED);
            session.setEndTime(LocalDateTime.now());
            if (reportUrl != null && !reportUrl.isBlank()) {
                session.setReportUrl(reportUrl);
            }
            
            // TODO: Dans une implémentation réelle, on pourrait appeler ici un service
            // d'évaluation automatique pour attribuer une note (grade)
            // session.setGrade(evaluationService.evaluateLab(session));
            
            LabSession updatedSession = labSessionRepository.save(session);
            
            log.info("Session de lab soumise - Session ID: {}, User: {}, Report URL: {}",
                    sessionId, session.getUser().getEmail(), reportUrl);
            
            return CResponse.success(updatedSession, "Session de lab soumise avec succès");
            
        } catch (Exception e) {
            log.error("Erreur lors de la soumission du lab: {}", e.getMessage(), e);
            return CResponse.error("Erreur lors de la soumission du lab: " + e.getMessage());
        }
    }
    
    /**
     * Récupère toutes les sessions d'un utilisateur.
     * @param userId ID de l'utilisateur
     * @return Liste des sessions de l'utilisateur
     */
    public CResponse<List<LabSession>> getUserSessions(Long userId) {
        try {
            List<LabSession> sessions = labSessionRepository.findByUserId(userId);
            return CResponse.success(sessions, "Sessions récupérées avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des sessions: {}", e.getMessage(), e);
            return CResponse.error("Erreur lors de la récupération des sessions: " + e.getMessage());
        }
    }
}
