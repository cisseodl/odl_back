package com.odc.aws_learning.app.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Calendar;

@Service
public class UploadFileService {

    @Autowired
    private AmazonS3 s3Client;

    private final String bucketName = "odl-learning-assets-prod";

    public String uploadFile(MultipartFile file, String folderName) throws IOException {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = Calendar.getInstance().getTimeInMillis() + "." + extension;
        String key = folderName + "/" + filename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try (InputStream is = file.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucketName, key, is, metadata));
        }
        return filename;
    }
    
    public String uploadInputStream(InputStream inputStream, String folderName, String fileName, long contentLength, String contentType) throws IOException {
        String key = folderName + "/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        metadata.setContentType(contentType);

        s3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, metadata));
        
        return fileName;
    }

    public String uploadBase64File(String base64File, String folderName, String fileType) throws IOException {
        String filename = Calendar.getInstance().getTimeInMillis() + "." + fileType;
        
        if (base64File.contains(",")) {
            base64File = base64File.split(",")[1];
        }

        byte[] bytes = Base64.getDecoder().decode(base64File);

        try (InputStream is = new ByteArrayInputStream(bytes)) {
            // Assuming the fileType is just the extension, e.g., "png"
            uploadInputStream(is, folderName, filename, bytes.length, "image/" + fileType);
        }

        return filename;
    }
}