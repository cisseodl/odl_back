package com.odc.aws_learning.app.controller;


import com.odc.aws_learning.app.entity.Reponses;
import com.odc.aws_learning.app.service.ReponsesService;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/reponses")
@RestController

public class ReponsesController {
    private final ReponsesService reponsesService;
    public ReponsesController(ReponsesService reponsesService) {
        this.reponsesService = reponsesService;

    }
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> saveReponses(@RequestBody Reponses reponses) {
        return reponsesService.saveReponses(reponses);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public CResponse<?> getAll() {
        return reponsesService.getAll();
    }
}


