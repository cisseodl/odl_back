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
    private final S3Service s3Service;
    private final UploadFileService uploadFileService;
    
    @Value("${file.upload-dir}")
    private String uploadDir;

    public RubriqueService(RubriqueRepository rubriqueRepository, UserRepository userRepository, S3Service s3Service, UploadFileService uploadFileService) {
        this.rubriqueRepository = rubriqueRepository;
        this.userRepository = userRepository;
        this.s3Service = s3Service;
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
            String imageUrl = s3Service.saveFile(imageFile, "rubriques");
            rubrique.setImage(imageUrl);
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
            // Optional: delete old image from S3 if it exists (seulement si c'est une URL S3)
            if (existingRubrique.getImage() != null && !existingRubrique.getImage().isEmpty()) {
                try {
                    // Ne supprimer que si c'est une URL S3 (contient le nom du bucket)
                    if (existingRubrique.getImage().contains("s3") || existingRubrique.getImage().contains("amazonaws.com")) {
                        s3Service.deleteFile(existingRubrique.getImage());
                    }
                } catch (Exception e) {
                    log.warn("Erreur lors de la suppression de l'ancienne image: {}", e.getMessage());
                }
            }
            
            try {
                // Essayer d'abord S3
                String newImageUrl = s3Service.saveFile(imageFile, "rubriques");
                if (newImageUrl != null && !newImageUrl.isEmpty()) {
                    existingRubrique.setImage(newImageUrl);
                }
            } catch (RuntimeException e) {
                // Si S3 échoue, utiliser le stockage local comme fallback
                log.warn("Échec de l'upload S3 pour l'image de la rubrique, utilisation du stockage local comme fallback: {}", e.getMessage());
                try {
                    String localFolderPath = uploadDir + "/rubriques";
                    String savedFileName = uploadFileService.uploadFile(imageFile, localFolderPath);
                    String localUrl = "http://localhost:8080/awsodclearning/api/files/rubriques/" + savedFileName;
                    log.info("Image de la rubrique sauvegardée localement: {}", localUrl);
                    existingRubrique.setImage(localUrl);
                } catch (IOException ioException) {
                    log.error("Erreur lors de la sauvegarde locale de l'image de la rubrique: {}", ioException.getMessage(), ioException);
                    // Continuer sans mettre à jour l'image si les deux méthodes échouent
                }
            }
        }

        return rubriqueRepository.save(existingRubrique);
    }

    public void deleteRubrique(Long id) {
        Rubrique rubrique = rubriqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rubrique not found with id: " + id));

        // Delete image from S3 before deleting the entity
        if (rubrique.getImage() != null && !rubrique.getImage().isEmpty()) {
            s3Service.deleteFile(rubrique.getImage());
        }

        rubriqueRepository.deleteById(id);
    }
}
