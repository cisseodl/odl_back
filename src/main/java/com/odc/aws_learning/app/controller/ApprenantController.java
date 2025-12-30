package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.entity.Apprenant;
import com.odc.aws_learning.app.service.ApprenantService;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/apprenants")
@RestController

public class ApprenantController {
    private final ApprenantService apprenantService;

    public ApprenantController(ApprenantService apprenantService) {
        this.apprenantService = apprenantService;
    }
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> saveApprenant(@RequestBody Apprenant apprenant) {
        return apprenantService.saveApprenant(apprenant);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public CResponse<?> getAll() {
        return apprenantService.getAll();
    }

    @GetMapping("/get-by-cohorte/{cohorteId}/{page}/{size}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public CResponse<?> getByCohorte(@PathVariable Long cohorteId, @PathVariable int page, @PathVariable int size) {
        return apprenantService.getByCohorte(cohorteId, page, size);
    }
}

