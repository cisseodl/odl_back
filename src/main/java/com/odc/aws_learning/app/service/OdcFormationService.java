package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.dto.OdcFormationDto;
import com.odc.aws_learning.app.dto.OdcFormationRequest;
import com.odc.aws_learning.app.entity.OdcFormation;
import com.odc.aws_learning.app.repository.OdcFormationRepository;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class OdcFormationService {

    private final OdcFormationRepository odcFormationRepository;

    public OdcFormationService(OdcFormationRepository odcFormationRepository) {
        this.odcFormationRepository = odcFormationRepository;
    }

    /**
     * Récupère toutes les formations ODC
     */
    @Transactional(readOnly = true)
    public List<OdcFormationDto> getAllFormations() {
        List<OdcFormation> formations = odcFormationRepository.findAll();
        return formations.stream()
                .map(OdcFormationDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Récupère une formation par son ID
     */
    @Transactional(readOnly = true)
    public Optional<OdcFormationDto> getFormationById(Long id) {
        return odcFormationRepository.findById(id)
                .map(OdcFormationDto::new);
    }

    /**
     * Crée une nouvelle formation ODC
     */
    public OdcFormationDto createFormation(OdcFormationRequest request, User admin) {
        if (admin == null || admin.getAdmin() == null) {
            throw new RuntimeException("Seuls les administrateurs peuvent créer des formations ODC");
        }

        OdcFormation formation = new OdcFormation();
        formation.setTitre(request.getTitre());
        formation.setDescription(request.getDescription());
        formation.setLien(request.getLien());
        formation.setAdmin(admin);
        formation.setActivate(true);

        OdcFormation savedFormation = odcFormationRepository.save(formation);
        log.info("Formation ODC créée avec succès: {} par l'admin {}", savedFormation.getId(), admin.getEmail());
        
        return new OdcFormationDto(savedFormation);
    }

    /**
     * Met à jour une formation ODC existante
     */
    public OdcFormationDto updateFormation(Long id, OdcFormationRequest request, User admin) {
        OdcFormation formation = odcFormationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation ODC non trouvée avec l'ID: " + id));

        // Vérifier que l'admin est bien le créateur de la formation ou un admin
        if (admin.getAdmin() == null) {
            throw new RuntimeException("Seuls les administrateurs peuvent modifier des formations ODC");
        }

        formation.setTitre(request.getTitre());
        formation.setDescription(request.getDescription());
        formation.setLien(request.getLien());

        OdcFormation updatedFormation = odcFormationRepository.save(formation);
        log.info("Formation ODC mise à jour avec succès: {} par l'admin {}", updatedFormation.getId(), admin.getEmail());
        
        return new OdcFormationDto(updatedFormation);
    }

    /**
     * Supprime une formation ODC
     */
    public void deleteFormation(Long id, User admin) {
        OdcFormation formation = odcFormationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation ODC non trouvée avec l'ID: " + id));

        if (admin.getAdmin() == null) {
            throw new RuntimeException("Seuls les administrateurs peuvent supprimer des formations ODC");
        }

        odcFormationRepository.deleteById(id);
        log.info("Formation ODC supprimée avec succès: {} par l'admin {}", id, admin.getEmail());
    }

    /**
     * Récupère toutes les formations créées par un admin spécifique
     */
    @Transactional(readOnly = true)
    public List<OdcFormationDto> getFormationsByAdmin(User admin) {
        List<OdcFormation> formations = odcFormationRepository.findByAdmin(admin);
        return formations.stream()
                .map(OdcFormationDto::new)
                .collect(Collectors.toList());
    }
}
