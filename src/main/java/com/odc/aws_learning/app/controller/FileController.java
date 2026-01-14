package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.service.UploadFileService;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${app.server.base-url:https://api.smart-odc.com}")
    private String serverBaseUrl;

    private final UploadFileService uploadFileService;

    @GetMapping("/{folderName}/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String folderName, @PathVariable String filename) {
        try {
            Path file = Paths.get(uploadDir).resolve(folderName).resolve(filename);
            log.debug("Tentative de récupération du fichier: {}", file.toAbsolutePath());
            
            Resource resource = new UrlResource(file.toUri());
            
            if (!resource.exists()) {
                log.warn("Fichier non trouvé: {} (chemin absolu: {})", file, file.toAbsolutePath());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            
            if (!resource.isReadable()) {
                log.warn("Fichier non lisible: {} (chemin absolu: {})", file, file.toAbsolutePath());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            
            // Détecter le type MIME du fichier
            String contentType = "application/octet-stream";
            String lowerFilename = filename.toLowerCase();
            if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (lowerFilename.endsWith(".png")) {
                contentType = "image/png";
            } else if (lowerFilename.endsWith(".gif")) {
                contentType = "image/gif";
            } else if (lowerFilename.endsWith(".webp")) {
                contentType = "image/webp";
            } else if (lowerFilename.endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (lowerFilename.endsWith(".mp4")) {
                contentType = "video/mp4";
            }
            
            log.debug("Fichier trouvé et servi: {} (type: {})", file, contentType);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du fichier {}/{}: {}", folderName, filename, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Endpoint pour l'upload de fichiers vers le stockage local (Elastic Beanstalk).
     * URL: POST /api/files/upload
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folderName", required = false, defaultValue = "test") String folderName) {
        
        if (file == null || file.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Le fichier est vide ou null");
        }

        try {
            // Utiliser le stockage local (Elastic Beanstalk)
            String localFolderPath = uploadDir + "/" + folderName;
            String savedFileName = uploadFileService.uploadFile(file, localFolderPath);
            // Générer l'URL complète pour servir le fichier via le FileController
            String fileUrl = serverBaseUrl + "/awsodclearning/api/files/" + folderName + "/" + savedFileName;
            log.info("Fichier sauvegardé localement: {}", fileUrl);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException ioException) {
            log.error("Erreur lors de la sauvegarde locale du fichier: {}", ioException.getMessage(), ioException);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'upload du fichier: " + ioException.getMessage());
        } catch (Exception e) {
            log.error("Erreur inattendue lors de l'upload: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur inattendue lors de l'upload: " + e.getMessage());
        }
    }

    @GetMapping("/presigned-url")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CResponse<?>> getPresignedUrl(
            @RequestParam String fileName,
            @RequestParam String fileType,
            @RequestParam(value = "folder", defaultValue = "") String folder) {
        // Pour Elastic Beanstalk, on retourne directement l'URL du fichier après upload
        // Cette méthode n'est plus utilisée avec S3, mais conservée pour compatibilité
        String fileUrl = serverBaseUrl + "/awsodclearning/api/files/" + folder + "/" + fileName;
        return ResponseEntity.ok(CResponse.success(fileUrl, "URL du fichier générée avec succès"));
    }
}
