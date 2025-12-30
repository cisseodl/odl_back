package com.odc.aws_learning.app.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odc.aws_learning.app.service.ChapterService;
import com.odc.aws_learning.app.wrapper.ChapterAndCoursePayload;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequestMapping("/chapters")
@RestController

public class ChapterController {
    private final ChapterService chapterService;
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public CResponse<?> saveChapter(@RequestParam("chapter") String chapterAndCoursePayloadsString,
                                    @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile) throws JsonProcessingException {
        ChapterAndCoursePayload chapterAndCoursePayload = new ObjectMapper().readValue(chapterAndCoursePayloadsString, ChapterAndCoursePayload.class);
        return chapterService.saveChapter(chapterAndCoursePayload, pdfFile);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER')")
    public CResponse<?> getChaptersByCourse(@PathVariable Long courseId) {
        return chapterService.getChaptersByCourse(courseId);
    }

//    @GetMapping("/get-all")
//    public CResponse<?> getAll() {
//        return ChapterService.getAll();
//    }
}
