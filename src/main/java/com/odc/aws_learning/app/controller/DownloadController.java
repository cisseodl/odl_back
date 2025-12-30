package com.odc.aws_learning.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;

@RestController
@RequestMapping("/downloads")
@RequiredArgsConstructor
public class DownloadController {
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    @GetMapping("/{folderName}/{fileName}")
    public void downloadFiles(HttpServletResponse response,
                              @PathVariable("fileName") String fileName,
                              @PathVariable("folderName") String resourceName) throws IOException {


        String path = uploadDir + "/" + resourceName;
        File file = new File(path + "/" + fileName);
        if (file.exists() && file.isFile()) {

            //get the mimetype
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
            response.setContentLength((int) file.length());

            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

            FileCopyUtils.copy(inputStream, response.getOutputStream());

        } else {
            // Retourner un 404 si le fichier n'existe pas
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("text/plain");
            response.getWriter().write("File not found: " + fileName);
        }
    }
}
