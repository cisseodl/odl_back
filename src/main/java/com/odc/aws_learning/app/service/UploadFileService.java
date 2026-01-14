package com.odc.aws_learning.app.service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

@Service
public class UploadFileService {
    public String uploadFile(MultipartFile photo, String link) throws IOException {
        Path uploadPath = Paths.get(link); // 'link' is "../Image/ODLearning"

        // Create the directory if it does not exist
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                // Log the error and rethrow or handle appropriately
                System.err.println("Error creating upload directories: " + uploadPath.toString() + " - " + e.getMessage());
                throw new IOException("Could not create upload directory: " + uploadPath.toString(), e);
            }
        }

        Calendar cal = Calendar.getInstance();
        Long millisDate = cal.getTimeInMillis();
        String extension = StringUtils.getFilenameExtension(photo.getOriginalFilename());
        String filename = millisDate.toString() + "." + extension;
        
        Path filePath = uploadPath.resolve(filename); // Resolve the full file path

        try (InputStream inputStream = photo.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING); // Copy to the full file path
        } catch (IOException e) {
            System.err.println("Error saving file: " + filePath.toString() + " - " + e.getMessage());
            throw new IOException("Could not save file: " + filePath.toString(), e);
        }
        
        return filename;
    }

    public String updateFile(MultipartFile photo, String link, String fileName) throws IOException {
        Path uploadPath = Paths.get(link); // 'link' is "../Image/ODLearning"
        
        // Ensure the directory exists for update operations as well
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                System.err.println("Error creating upload directories for update: " + uploadPath.toString() + " - " + e.getMessage());
                throw new IOException("Could not create upload directory for update: " + uploadPath.toString(), e);
            }
        }

        Path filePath = uploadPath.resolve(fileName);
        try (InputStream inputStream = photo.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error updating file: " + filePath.toString() + " - " + e.getMessage());
            throw new IOException("Could not update file: " + filePath.toString(), e);
        }
        return fileName;
    }

    public String uploadBase64File(String base64File, String link, String fileType) throws IOException {
        Path uploadPath = Paths.get(link);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                System.err.println("Error creating upload directories for base64: " + uploadPath.toString() + " - " + e.getMessage());
                throw new IOException("Could not create upload directory for base64: " + uploadPath.toString(), e);
            }
        }

        Calendar cal = Calendar.getInstance();
        Long millisDate = cal.getTimeInMillis();
        String filename = millisDate.toString() + "." + fileType;
        Path filePath = uploadPath.resolve(filename);

        byte[] imageByte = Base64.decodeBase64(base64File);
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) { // Convert Path to File
            fos.write(imageByte);
        } catch (IOException e) {
            System.err.println("Error saving base64 file: " + filePath.toString() + " - " + e.getMessage());
            throw new IOException("Could not save base64 file: " + filePath.toString(), e);
        }
        return filename;
    }

    /**
     * Upload un fichier à partir d'un InputStream vers le stockage local.
     * @param inputStream Le flux de données du fichier.
     * @param link Le chemin du dossier de destination.
     * @param fileName Le nom du fichier à sauvegarder.
     * @return Le nom du fichier sauvegardé.
     * @throws IOException En cas d'erreur lors de l'écriture du fichier.
     */
    public String uploadFileFromInputStream(InputStream inputStream, String link, String fileName) throws IOException {
        Path uploadPath = Paths.get(link);
        
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                System.err.println("Error creating upload directories for InputStream: " + uploadPath.toString() + " - " + e.getMessage());
                throw new IOException("Could not create upload directory for InputStream: " + uploadPath.toString(), e);
            }
        }

        Path filePath = uploadPath.resolve(fileName);
        try {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error saving file from InputStream: " + filePath.toString() + " - " + e.getMessage());
            throw new IOException("Could not save file from InputStream: " + filePath.toString(), e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing InputStream: " + e.getMessage());
            }
        }
        
        return fileName;
    }
}
