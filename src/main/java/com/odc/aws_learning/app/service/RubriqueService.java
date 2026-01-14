package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Rubrique;
import com.odc.aws_learning.app.repository.RubriqueRepository;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
// import lombok.AllArgsConstructor; // Lombok removed
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
// @AllArgsConstructor // Lombok removed
public class RubriqueService {

    private final RubriqueRepository rubriqueRepository;
    private final UserRepository userRepository;
    private final UploadFileService uploadFileService;
    
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${app.server.base-url:https://api.smart-odc.com}")
    private String serverBaseUrl;

    public RubriqueService(RubriqueRepository rubriqueRepository, UserRepository userRepository, UploadFileService uploadFileService) {
        this.rubriqueRepository = rubriqueRepository;
        this.userRepository = userRepository;
        this.uploadFileService = uploadFileService;
    }

    public List<Rubrique> getAllRubriques() {
        return rubriqueRepository.findAll();
    }

    public Optional<Rubrique> getRubriqueById(Long id) {
        return rubriqueRepository.findById(id);
    }

    public Rubrique saveRubrique(Rubrique rubrique, MultipartFile imageFile, String creatorEmail) throws IOException {
        // The createdBy field is now automatically managed by Spring Data JPA Auditing
        // based on the authenticated user's email. So, we don't need to manually set it here.
        // However, if the Rubrique entity still needs a direct User relationship for other reasons,
        // this method signature and logic would need to be re-evaluated.
        // For now, we rely on auditing for createdBy.

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Utiliser le stockage local (Elastic Beanstalk)
                String localFolderPath = uploadDir + "/rubriques";
                String savedFileName = uploadFileService.uploadFile(imageFile, localFolderPath);
                String imageUrl = serverBaseUrl + "/awsodclearning/api/files/rubriques/" + savedFileName;
                log.info("Image de la rubrique sauvegardée localement: {}", imageUrl);
                rubrique.setImage(imageUrl);
            } catch (IOException ioException) {
                log.error("Erreur lors de la sauvegarde locale de l'image de la rubrique: {}", ioException.getMessage(), ioException);
                // Continuer sans image si l'upload échoue
            }
        }

        return rubriqueRepository.save(rubrique);
    }

    public Rubrique updateRubrique(Long id, Rubrique rubriqueDetails, MultipartFile imageFile) throws IOException {
        Rubrique existingRubrique = rubriqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rubrique not found with id: " + id));

        existingRubrique.setRubrique(rubriqueDetails.getRubrique());
        existingRubrique.setDescription(rubriqueDetails.getDescription());
        existingRubrique.setObjectifs(rubriqueDetails.getObjectifs());
        existingRubrique.setPublicCible(rubriqueDetails.getPublicCible());
        existingRubrique.setDureeFormat(rubriqueDetails.getDureeFormat());
        existingRubrique.setLienRessources(rubriqueDetails.getLienRessources());

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Utiliser le stockage local (Elastic Beanstalk)
                String localFolderPath = uploadDir + "/rubriques";
                String savedFileName = uploadFileService.uploadFile(imageFile, localFolderPath);
                String imageUrl = serverBaseUrl + "/awsodclearning/api/files/rubriques/" + savedFileName;
                log.info("Image de la rubrique mise à jour localement: {}", imageUrl);
                existingRubrique.setImage(imageUrl);
            } catch (IOException ioException) {
                log.error("Erreur lors de la sauvegarde locale de l'image de la rubrique: {}", ioException.getMessage(), ioException);
                // Continuer sans mettre à jour l'image si l'upload échoue
            }
        }

        return rubriqueRepository.save(existingRubrique);
    }

    public void deleteRubrique(Long id) {
        Rubrique rubrique = rubriqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rubrique not found with id: " + id));

        // Note: Avec le stockage local, les fichiers sont stockés sur le système de fichiers
        // Elastic Beanstalk. La suppression des fichiers peut être gérée par un job de nettoyage
        // ou manuellement si nécessaire. Pour l'instant, on supprime juste l'entité.

        rubriqueRepository.deleteById(id);
    }
}
