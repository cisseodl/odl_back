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
        Path path = Paths.get(link);
        Calendar cal = Calendar.getInstance();
        Long millisDate = cal.getTimeInMillis();
        String extension = StringUtils.getFilenameExtension(photo.getOriginalFilename());
        String filename = millisDate.toString() + "." + extension;
        InputStream inputStream = photo.getInputStream();
        Files.copy(inputStream, path.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        return filename;

//        Files.copy(inputStream, path.resolve(millisDate + photo.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
//        return millisDate.toString() + photo.getOriginalFilename();
    }

    public String updateFile(MultipartFile photo, String link, String fileName) throws IOException {
        Path path = Paths.get(link);
        InputStream inputStream = photo.getInputStream();
        Files.copy(inputStream, path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public String uploadBase64File(String base64File, String link, String fileType) throws IOException {
        Calendar cal = Calendar.getInstance();
        Long millisDate = cal.getTimeInMillis();
        String path = link + "/" + millisDate.toString() + "." + fileType;
        byte[] imageByte= Base64.decodeBase64(base64File);
        new FileOutputStream(path).write(imageByte);
        return millisDate + "." + fileType;
    }
}
