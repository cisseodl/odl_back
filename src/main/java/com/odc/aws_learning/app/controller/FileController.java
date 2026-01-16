package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.service.UploadFileService;
import com.odc.aws_learning.auth.base.response.CResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final UploadFileService uploadFileService;

    private final String BUCKET_NAME = "odl-learning-assets-prod";
    private final String REGION = "us-east-1";

    /**
     * POST /api/files/upload
     * Retourne directement l'URL S3. Plus besoin de serveFile !
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folderName", required = false, defaultValue = "documents") String folderName) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fichier vide");
        }

        try {
            String savedFileName = uploadFileService.uploadFile(file, folderName);

            // Génération de l'URL finale S3
            String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s/%s",
                    BUCKET_NAME, REGION, folderName, savedFileName);

            log.info("Fichier disponible sur S3 : {}", fileUrl);
            return ResponseEntity.ok(fileUrl);

        } catch (Exception e) {
            log.error("Erreur S3 : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur d'upload : " + e.getMessage());
        }
    }

    /**
     * Pour récupérer l'URL d'un fichier dont on connaît déjà le nom en base de données.
     */
    @GetMapping("/url")
    public ResponseEntity<String> getFileUrl(
            @RequestParam String fileName,
            @RequestParam(value = "folder", defaultValue = "documents") String folder) {

        String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s/%s",
                BUCKET_NAME, REGION, folder, fileName);

        return ResponseEntity.ok(fileUrl);
    }
}