package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.entity.Configuration;
import com.odc.aws_learning.app.service.ConfigurationService;
import com.odc.aws_learning.app.wrapper.ConfigurationDto;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/configurations")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping("/get-config")
    public CResponse<?> getConfiguration() {
        return configurationService.getConfiguration();
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> updateConfiguration(
            @RequestParam(value = "homepageText", required = false) String homepageText,
            @RequestParam(value = "homepageImage", required = false) MultipartFile homepageImage,
            @RequestParam(value = "loginImage", required = false) MultipartFile loginImage,
            @RequestParam(value = "aboutText", required = false) String aboutText,
            @RequestParam(value = "aboutImage", required = false) MultipartFile aboutImage) {

        return configurationService.updateConfiguration(homepageText, homepageImage, loginImage, aboutText, aboutImage);
    }
}