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
import java.util.UUID;
import java.util.Date;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import java.io.InputStream; // Ajouté
import org.slf4j.Logger; // Ajouté
import org.slf4j.LoggerFactory; // Ajouté

@Service
//@Slf4j // Supprimé
@RequiredArgsConstructor
public class S3Service {

    private static final Logger log = LoggerFactory.getLogger(S3Service.class); // Ajouté

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

        try { // Nouveau bloc try
            try (InputStream inputStream = file.getInputStream()) {
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);

                amazonS3.putObject(putObjectRequest);

                String fileUrl = amazonS3.getUrl(bucketName, key).toString();
                log.info("Fichier uploadé sur S3: {}", fileUrl);
                return fileUrl;
            }
        } catch (AmazonServiceException e) {
            log.error("Erreur AWS lors de l'upload du fichier vers S3. Code d'erreur: {}, Message: {}, Détails: {}", 
                e.getErrorCode(), e.getMessage(), e.getErrorMessage(), e);
            throw new RuntimeException("Erreur AWS S3: " + e.getMessage() + " (Code: " + e.getErrorCode() + ")", e);
        } catch (IOException e) {
            log.error("Erreur IO lors de l'obtention de l'InputStream ou de la fermeture: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur IO lors de l'upload: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Erreur inattendue lors de l'upload du fichier vers S3: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur inattendue lors de l'upload: " + e.getMessage(), e);
        }
    }

    /**
     * Sauvegarde un fichier à partir d'un InputStream dans S3 et renvoie l'URL publique.
     * @param inputStream Le flux de données du fichier.
     * @param contentLength La taille du fichier en octets.
     * @param contentType Le type MIME du fichier (ex: "application/pdf").
     * @param fileName Le nom du fichier original ou souhaité (avec extension).
     * @param folderName Le dossier dans S3 où le fichier sera stocké.
     * @return L'URL publique du fichier sur S3, ou null en cas d'erreur.
     */
    public String saveFile(InputStream inputStream, long contentLength, String contentType, String fileName, String folderName) {
        String originalFilename = fileName; // Utilisez fileName comme base pour l'extension
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }

        String key = buildKey(folderName, extension);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        metadata.setContentType(contentType);

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3.putObject(putObjectRequest);

            String fileUrl = amazonS3.getUrl(bucketName, key).toString();
            log.info("Fichier uploadé sur S3: {}", fileUrl);
            return fileUrl;
        } catch (AmazonServiceException e) {
            log.error("Erreur AWS lors de l'upload du fichier vers S3: {}", e.getMessage(), e);
            return null;
        } catch (Exception e) { // IOException est déjà géré par AmazonServiceException, ou le compilateur se plaindra
            log.error("Erreur inattendue lors de l'upload du fichier vers S3: {}", e.getMessage(), e);
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("Erreur lors de la fermeture de l'InputStream: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Génère une URL pré-signée pour un upload de fichier direct vers S3.
     * Le client pourra utiliser cette URL PUT pour uploader directement le fichier.
     * La durée de validité de l'URL est de 5 minutes.
     * @param fileName Le nom du fichier tel qu'il sera stocké dans S3 (peut inclure un chemin).
     * @param fileType Le type MIME du fichier (ex: "image/jpeg").
     * @param folder Le dossier dans lequel le fichier sera uploadé (ex: "avatars/", "cours/").
     * @return L'URL pré-signée sous forme de String.
     */
    public String generatePresignedUrl(String fileName, String fileType, String folder) {
        try {
            // Définir l'expiration de l'URL (ici, 5 minutes)
            Date expiration = new Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 5; // 5 minutes
            expiration.setTime(expTimeMillis);

            String key = (folder.isEmpty() ? "" : folder + "/") + fileName;

            // Générer l'URL pré-signée pour un PUT
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, key)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration);

            // Ajouter le ContentType pour que S3 le reconnaisse lors de l'upload
            ResponseHeaderOverrides overrides = new ResponseHeaderOverrides();
            overrides.setContentType(fileType);
            generatePresignedUrlRequest.setResponseHeaders(overrides);


            String presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
            log.info("URL pré-signée générée pour {}: {}", key, presignedUrl);
            return presignedUrl;
        } catch (AmazonServiceException e) {
            log.error("Erreur AWS lors de la génération de l'URL pré-signée: {}", e.getMessage(), e);
            return null;
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la génération de l'URL pré-signée: {}", e.getMessage(), e);
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

    /**
     * Supprime un fichier de S3 en utilisant son URL complète.
     * @param fileUrl L'URL complète du fichier à supprimer.
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty() || !fileUrl.contains(bucketName)) {
            log.warn("URL de fichier invalide ou vide fournie pour la suppression: {}", fileUrl);
            return;
        }

        try {
            // Extrait la clé de l'objet à partir de l'URL
            // L'URL est généralement de la forme : https://<bucket-name>.s3.<region>.amazonaws.com/<key>
            String key = fileUrl.substring(fileUrl.indexOf(bucketName + "/") + bucketName.length() + 1);

            amazonS3.deleteObject(bucketName, key);
            log.info("Fichier supprimé de S3 avec la clé: {}", key);
        } catch (AmazonServiceException e) {
            log.error("Erreur AWS lors de la suppression du fichier de S3: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la suppression du fichier de S3: {}", e.getMessage(), e);
        }
    }
}
