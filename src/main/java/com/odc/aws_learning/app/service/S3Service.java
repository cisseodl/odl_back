package com.odc.aws_learning.app.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    /**
     * Sauvegarde un fichier dans S3 sous un dossier donné et renvoie l'URL publique.
     */
    public String saveFile(MultipartFile file, String folderName) {
        if (file == null || file.isEmpty()) {
            log.warn("Tentative d'upload d'un fichier vide vers S3");
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }

        String key = buildKey(folderName, extension);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3.putObject(putObjectRequest);

            String fileUrl = amazonS3.getUrl(bucketName, key).toString();
            log.info("Fichier uploadé sur S3: {}", fileUrl);
            return fileUrl;
        } catch (AmazonServiceException e) {
            log.error("Erreur AWS lors de l'upload du fichier vers S3: {}", e.getMessage(), e);
            return null;
        } catch (IOException e) {
            log.error("Erreur IO lors de la lecture du fichier pour S3: {}", e.getMessage(), e);
            return null;
        }
    }

    private String buildKey(String folderName, String extension) {
        String uuid = UUID.randomUUID().toString();
        String normalizedFolder = folderName != null ? folderName.trim() : "";
        if (!normalizedFolder.isEmpty()) {
            if (!normalizedFolder.endsWith("/")) {
                normalizedFolder = normalizedFolder + "/";
            }
        }
        return normalizedFolder + uuid + extension;
    }
}
