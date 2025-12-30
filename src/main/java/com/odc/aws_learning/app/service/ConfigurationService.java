package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.constante.UploadLink;
import com.odc.aws_learning.app.repository.ConfigurationRepository;
import com.odc.aws_learning.app.entity.Configuration;
import com.odc.aws_learning.app.wrapper.ConfigurationDto;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final String uploadDir;

    public ConfigurationService(ConfigurationRepository configurationRepository, @Value("${file.upload-dir}") String uploadDir) {
        this.configurationRepository = configurationRepository;
        this.uploadDir = uploadDir;
    }

    public CResponse<?> getConfiguration() {
        Configuration config = configurationRepository.findTopByOrderByIdDesc();
        if (config == null) {
            ConfigurationDto defaultDto = new ConfigurationDto();
            defaultDto.setHomepageText("Commencez à prendre avec Orange Digital Learning ODL.");
            // Ne pas retourner d'URL d'image fictive - laisser null pour utiliser l'image de fallback côté frontend
            defaultDto.setHomepageImageUrl(null);
            defaultDto.setLoginImageUrl(null);
            defaultDto.setAboutText("Découvrez notre mission et notre équipe chez ODL.");
            defaultDto.setAboutImageUrl(null);
            return CResponse.success(defaultDto, "Configuration par défaut");
        }
        return CResponse.success(convertToDto(config), "Configuration");
    }

    public CResponse<?> updateConfiguration(
            @RequestParam(value = "homepageText", required = false) String homepageText,
            @RequestParam(value = "homepageImage", required = false) MultipartFile homepageImage,
            @RequestParam(value = "loginImage", required = false) MultipartFile loginImage,
            @RequestParam(value = "aboutText", required = false) String aboutText,
            @RequestParam(value = "aboutImage", required = false) MultipartFile aboutImage) {

        Configuration config = configurationRepository.findTopByOrderByIdDesc();
        if (config == null) {
            config = new Configuration();
        }

        if (homepageText != null && !homepageText.isEmpty()) {
            config.setHomepageText(homepageText);
        }
        if (homepageImage != null && !homepageImage.isEmpty()) {
            String homepageImageUrl = saveFile(homepageImage);
            config.setHomepageImageUrl(homepageImageUrl);
        }
        if (loginImage != null && !loginImage.isEmpty()) {
            String loginImageUrl = saveFile(loginImage);
            config.setLoginImageUrl(loginImageUrl);
        }
        if (aboutText != null && !aboutText.isEmpty()) {
            config.setAboutText(aboutText);
        }
        if (aboutImage != null && !aboutImage.isEmpty()) {
            String aboutImageUrl = saveFile(aboutImage);
            config.setAboutImageUrl(aboutImageUrl);
        }

        Configuration configurationSaved = configurationRepository.save(config);
        return CResponse.success(configurationSaved, "Configuration mise à jour");
    }

    private String saveFile(MultipartFile file) {
        File directory = new File(uploadDir + "/config");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + originalFilename;
        Path filePath = Paths.get(uploadDir + "/config", fileName);
        try {
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Échec de la sauvegarde du fichier: " + e.getMessage());
        }
        return "/config/" + fileName;
    }

    private ConfigurationDto convertToDto(Configuration config) {
        ConfigurationDto dto = new ConfigurationDto();
        dto.setHomepageText(config.getHomepageText());
        dto.setHomepageImageUrl(config.getHomepageImageUrl());
        dto.setLoginImageUrl(config.getLoginImageUrl());
        dto.setAboutText(config.getAboutText());
        dto.setAboutImageUrl(config.getAboutImageUrl());
        return dto;
    }
}