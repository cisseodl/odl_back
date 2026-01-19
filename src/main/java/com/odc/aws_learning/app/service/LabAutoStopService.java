package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.LabSession;
import com.odc.aws_learning.app.entity.LabSessionStatus;
import com.odc.aws_learning.app.repository.LabSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

/**
 * Service pour l'arrêt automatique des sessions de lab qui ont dépassé leur durée maximale.
 * 
 * Ce service vérifie périodiquement toutes les sessions actives (RUNNING ou STARTING)
 * et arrête automatiquement celles qui ont dépassé la durée maximale définie dans
 * la LabDefinition associée.
 */
@Service
public class LabAutoStopService {
    
    private static final Logger log = LoggerFactory.getLogger(LabAutoStopService.class);
    
    @Autowired
    private LabSessionRepository labSessionRepository;
    
    @Autowired
    private LabService labService;
    
    /**
     * Vérifie et arrête automatiquement les sessions de lab qui ont dépassé leur durée maximale.
     * 
     * Cette méthode s'exécute toutes les 5 minutes (300 000 millisecondes).
     * Elle :
     * 1. Trouve toutes les sessions actives (RUNNING ou STARTING)
     * 2. Pour chaque session, vérifie si startTime + maxDurationMinutes a été dépassé
     * 3. Si oui, arrête automatiquement la session
     */
    @Scheduled(fixedRate = 300000) // Exécution toutes les 5 minutes (300 000 ms)
    @Transactional
    public void checkAndStopExpiredLabSessions() {
        try {
            log.debug("Démarrage de la vérification des sessions de lab expirées...");
            
            // 1. Trouver toutes les sessions actives (RUNNING ou STARTING)
            List<LabSessionStatus> activeStatuses = Arrays.asList(
                    LabSessionStatus.STARTING,
                    LabSessionStatus.RUNNING
            );
            
            List<LabSession> activeSessions = labSessionRepository.findByStatusIn(activeStatuses);
            
            if (activeSessions.isEmpty()) {
                log.debug("Aucune session active trouvée.");
                return;
            }
            
            log.debug("{} session(s) active(s) trouvée(s). Vérification des durées...", activeSessions.size());
            
            LocalDateTime now = LocalDateTime.now();
            int stoppedCount = 0;
            
            // 2. Pour chaque session active, vérifier si elle a dépassé la durée maximale
            for (LabSession session : activeSessions) {
                try {
                    // Vérifier que la session a un startTime et une labDefinition avec maxDurationMinutes
                    if (session.getStartTime() == null) {
                        log.warn("Session {} n'a pas de startTime, ignorée.", session.getId());
                        continue;
                    }
                    
                    if (session.getLabDefinition() == null) {
                        log.warn("Session {} n'a pas de labDefinition associée, ignorée.", session.getId());
                        continue;
                    }
                    
                    Integer maxDurationMinutes = session.getLabDefinition().getMaxDurationMinutes();
                    if (maxDurationMinutes == null || maxDurationMinutes <= 0) {
                        log.debug("Session {} n'a pas de maxDurationMinutes défini, ignorée.", session.getId());
                        continue;
                    }
                    
                    // Calculer la date/heure d'expiration
                    LocalDateTime expirationTime = session.getStartTime().plusMinutes(maxDurationMinutes);
                    
                    // Vérifier si la session a expiré
                    if (now.isAfter(expirationTime) || now.isEqual(expirationTime)) {
                        log.info("Session {} a dépassé sa durée maximale ({} minutes). Arrêt automatique...",
                                session.getId(), maxDurationMinutes);
                        
                        // Calculer le temps écoulé pour le log
                        long elapsedMinutes = ChronoUnit.MINUTES.between(session.getStartTime(), now);
                        log.info("Temps écoulé: {} minutes (max: {} minutes)", elapsedMinutes, maxDurationMinutes);
                        
                        // Arrêter la session en utilisant le service existant
                        var result = labService.stopLab(session.getId());
                        
                        if (result.isOk()) {
                            stoppedCount++;
                            log.info("Session {} arrêtée automatiquement avec succès.", session.getId());
                        } else {
                            log.error("Erreur lors de l'arrêt automatique de la session {}: {}",
                                    session.getId(), result.getMessage());
                        }
                    } else {
                        // Calculer le temps restant pour le debug
                        long remainingMinutes = ChronoUnit.MINUTES.between(now, expirationTime);
                        log.debug("Session {} encore active. Temps restant: {} minutes (max: {} minutes)",
                                session.getId(), remainingMinutes, maxDurationMinutes);
                    }
                    
                } catch (Exception e) {
                    log.error("Erreur lors du traitement de la session {}: {}",
                            session.getId(), e.getMessage(), e);
                }
            }
            
            if (stoppedCount > 0) {
                log.info("Vérification terminée. {} session(s) arrêtée(s) automatiquement.", stoppedCount);
            } else {
                log.debug("Vérification terminée. Aucune session expirée.");
            }
            
        } catch (Exception e) {
            log.error("Erreur lors de la vérification des sessions expirées: {}", e.getMessage(), e);
        }
    }
}

