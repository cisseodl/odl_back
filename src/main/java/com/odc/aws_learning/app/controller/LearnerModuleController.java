package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.service.LearnerService;
import com.odc.aws_learning.app.wrapper.ValidateModule;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/learnermodule")
@RestController
public class LearnerModuleController {

    private final LearnerService learnerService;

    public LearnerModuleController(LearnerService learnerService) {
        this.learnerService = learnerService;
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public CResponse<?> saveLearner(ValidateModule validateModule){
        return learnerService.saveLearner(validateModule);
    }

}
