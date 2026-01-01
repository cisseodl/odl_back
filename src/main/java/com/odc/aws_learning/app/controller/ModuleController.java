package com.odc.aws_learning.app.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odc.aws_learning.app.service.ModuleService;
import com.odc.aws_learning.app.wrapper.ModuleAndCoursePayload;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequestMapping("/modules")
@RestController

public class ModuleController {
    private final ModuleService moduleService;
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> saveModule(@RequestParam("module") String moduleAndCoursePayloadsString,
                                    @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile) throws JsonProcessingException {
        ModuleAndCoursePayload moduleAndCoursePayload = new ObjectMapper().readValue(moduleAndCoursePayloadsString, ModuleAndCoursePayload.class);
        return moduleService.saveModule(moduleAndCoursePayload, pdfFile);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public CResponse<?> getModulesByCourse(@PathVariable Long courseId) {
        return moduleService.getModulesByCourse(courseId);
    }


}
