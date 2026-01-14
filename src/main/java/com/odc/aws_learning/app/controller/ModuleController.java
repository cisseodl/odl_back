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
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public CResponse<?> saveModule(@RequestParam("module") String moduleAndCoursePayloadsString,
                                    @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile) throws JsonProcessingException {
        System.out.println("=== RECEPTION DU PAYLOAD MODULES ===");
        System.out.println("Payload JSON reçu: " + moduleAndCoursePayloadsString);
        
        ModuleAndCoursePayload moduleAndCoursePayload = new ObjectMapper().readValue(moduleAndCoursePayloadsString, ModuleAndCoursePayload.class);
        
        System.out.println("CourseId: " + moduleAndCoursePayload.getCourseId());
        System.out.println("Nombre de modules: " + (moduleAndCoursePayload.getModules() != null ? moduleAndCoursePayload.getModules().size() : 0));
        
        if (moduleAndCoursePayload.getModules() != null) {
            for (int i = 0; i < moduleAndCoursePayload.getModules().size(); i++) {
                var m = moduleAndCoursePayload.getModules().get(i);
                System.out.println("Module " + i + ": " + m.getTitle() + ", Leçons: " + (m.getLessons() != null ? m.getLessons().size() : 0));
                if (m.getLessons() != null) {
                    for (int j = 0; j < m.getLessons().size(); j++) {
                        var l = m.getLessons().get(j);
                        System.out.println("  Leçon " + j + ": " + l.getTitle() + ", Type: " + l.getType());
                    }
                }
            }
        }
        
        return moduleService.saveModule(moduleAndCoursePayload, pdfFile);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT', 'INSTRUCTOR')")
    public CResponse<?> getModulesByCourse(@PathVariable Long courseId) {
        return moduleService.getModulesByCourse(courseId);
    }


}
