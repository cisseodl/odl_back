package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.entity.Answer;

import com.odc.aws_learning.app.service.AnswerService;
import com.odc.aws_learning.app.wrapper.Evaluations_QuestionsReponses;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/answers")
@RestController

public class AnswerController {
    private  final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> saveAwsers(@RequestBody Answer answer) {
        return answerService.saveAnswer(answer);
    }

    @PostMapping("/save-learner-test")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT')")
    public CResponse<?> saveLearnerTest(@RequestBody Evaluations_QuestionsReponses evaluations_questionsReponses) {
        System.err.println(evaluations_questionsReponses.getEvaluationId());
        return answerService.saveLearnerTest(evaluations_questionsReponses);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'APPRENANT')")
    public CResponse<?> getAll() {
        return answerService.getAll();
    }
}

