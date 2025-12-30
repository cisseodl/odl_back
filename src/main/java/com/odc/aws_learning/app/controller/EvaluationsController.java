package com.odc.aws_learning.app.controller;


import com.odc.aws_learning.app.service.EvaluationsService;
import com.odc.aws_learning.app.wrapper.Quiz_Answer;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/evaluations")
@RestController

public class EvaluationsController {
    private final EvaluationsService evaluationsService;
    public EvaluationsController(EvaluationsService evaluationsService) {
        this.evaluationsService = evaluationsService;
    }
    @PostMapping("/save")
    public CResponse<?> saveEvaluations(@RequestBody Quiz_Answer quiz_answer) {
        return evaluationsService.createEvaluation(quiz_answer);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public CResponse<?> getAll() {
        return evaluationsService.getAll();
    }
}
