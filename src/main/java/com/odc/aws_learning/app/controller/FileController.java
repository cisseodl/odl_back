package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.service.S3Service;
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

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final S3Service s3Service;
    private final UploadFileService uploadFileService;

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Path file = Paths.get(uploadDir).resolve(filename);
        Resource resource;
        try {
            resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Le fichier n'est pas lisible");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Endpoint de test pour l'upload vers S3.
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
            String url = s3Service.saveFile(file, folderName);
            if (url != null && !url.isEmpty()) {
                return ResponseEntity.ok(url);
            } else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erreur lors de l'upload du fichier vers S3: Le service a retourné null. Vérifiez les logs du serveur et la configuration AWS (credentials, bucket, région).");
            }
        } catch (RuntimeException e) {
            // Les exceptions sont maintenant propagées depuis S3Service
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'upload du fichier vers S3: " + e.getMessage());
        } catch (Exception e) {
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
        String presignedUrl = s3Service.generatePresignedUrl(fileName, fileType, folder);
        if (presignedUrl != null) {
            return ResponseEntity.ok(CResponse.success(presignedUrl, "URL pré-signée générée avec succès"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CResponse.error("Erreur lors de la génération de l'URL pré-signée"));
        }
    }
}
