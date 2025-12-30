package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.entity.Cohorte;
import com.odc.aws_learning.app.service.CohorteService;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/cohorte")
@RestController

public class CohorteController {

    private final CohorteService cohorteService;

    public CohorteController(CohorteService cohorteService) {
        this.cohorteService = cohorteService;
    }

    @GetMapping("/read")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public CResponse<?> getAllCohortes() {
        return cohorteService.getAllCohortes();
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public CResponse<?> getCohorteById(@PathVariable Long id) {
        return cohorteService.getCohorteById(id);

    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> createCohorte(@RequestBody Cohorte cohorte) {
           return cohorteService.createCohorte(cohorte);

    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> updateCohorte(@RequestBody Cohorte cohorte) {
       return cohorteService.updateCohorte(cohorte);


    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> deleteCohorte(@PathVariable Long id) {

        return cohorteService.deleteCohorte(id);

    }
}
