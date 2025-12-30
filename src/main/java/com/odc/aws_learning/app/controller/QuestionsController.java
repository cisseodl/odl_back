package com.odc.aws_learning.app.controller;

import com.odc.aws_learning.app.entity.Questions;
import com.odc.aws_learning.app.service.QuestionsService;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/questions")
@RestController
public class QuestionsController {
    private final QuestionsService questionsService;
    public QuestionsController(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> saveQuestions(@RequestBody Questions questions) {
        return questionsService.saveQuestions(questions);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public CResponse<?> getAll() {
        return questionsService.getAll();
    }
}
