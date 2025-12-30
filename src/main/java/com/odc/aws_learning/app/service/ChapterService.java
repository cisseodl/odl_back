package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.constante.UploadLink;
import com.odc.aws_learning.app.entity.Chapter;
import com.odc.aws_learning.app.entity.Courses;
import com.odc.aws_learning.app.repository.ChapterRepository;
import com.odc.aws_learning.app.repository.CoursesRepository;
import com.odc.aws_learning.app.wrapper.ChapterAndCoursePayload;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final CoursesRepository coursesRepository;
    private final UploadFileService uploadFileService;

    public ChapterService(ChapterRepository chapterRepository, CoursesRepository coursesRepository, UploadFileService uploadFileService) {
        this.chapterRepository = chapterRepository;
        this.coursesRepository = coursesRepository;
        this.uploadFileService = uploadFileService;
    }

    public CResponse<?> saveChapter(ChapterAndCoursePayload chapterAndCoursePayload, MultipartFile pdfFile) {
        try {
             Optional<Courses> coursesOptional = coursesRepository.findById(chapterAndCoursePayload.getCourseId());
            if (coursesOptional.isPresent()) {
                removeOldChpterToCourse(chapterAndCoursePayload.getCourseId());
                coursesOptional.get().setCourseType(chapterAndCoursePayload.courseType);
                coursesRepository.save(coursesOptional.get());
                if(!chapterAndCoursePayload.getChapters().isEmpty()) {
                    List<Chapter> chapterList = new ArrayList<>();
                    chapterAndCoursePayload.getChapters().forEach(chapter -> {
                        Chapter chapterToSave = new Chapter();
                        chapterToSave.setCourse(coursesOptional.get());
                        chapterToSave.setDescription(chapter.getDescription());
                        chapterToSave.setTitle(chapter.getTitle());
                        chapterToSave.setChapterLink(chapter.getChapterLink());
                        if (pdfFile != null) {
                            System.err.println("PDF: " + pdfFile.getOriginalFilename());
                            try {
                                chapterToSave.setPdfPath(uploadFileService.uploadFile(pdfFile, UploadLink.DOWNLOAD_LINK + "/cours"));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        chapterList.add(chapterToSave);
                    });
                    List<Chapter> chapterListSaved = chapterRepository.saveAll(chapterList);
                    return CResponse.success(chapterListSaved.size(), "Chapitres enregistrés avec succès");
                }

//                if (pdfFile != null) {
//                    Chapter chapter = new Chapter();
//                    chapter.setCourse(coursesOptional.get());
//                    chapter.setPdfPath(uploadFileService.uploadFile(pdfFile, UploadLink.DOWNLOAD_LINK + "/cours"));
//                }

                return CResponse.error("Attention, la liste des chapitres est vide");

            }
            return CResponse.error("Cours introuvable");

        } catch (Exception e) {
            return CResponse.error("Error d'enregistrement");
        }
    }

    void removeOldChpterToCourse(Long courseId) {
        List<Chapter> chapters = chapterRepository.findAllByActivateAndCourseId(true, courseId);
        if (!chapters.isEmpty()) {
            chapters.forEach(chapter -> {
                chapter.setCourse(null);
                chapterRepository.save(chapter);
            });
        }
    }

    public CResponse<?> getChaptersByCourse(Long courseId) {
        try {
            Optional<Courses> coursesOptional = coursesRepository.findById(courseId);
            if (coursesOptional.isPresent()) {
                List<Chapter> chapters = chapterRepository.findAllByActivateAndCourseId(true, courseId);
                return CResponse.success(chapters, "Chapitres");
            }
            return CResponse.error("Cours introuvable");
        } catch (Exception e) {
            return CResponse.error("Erreur de récupération");
        }
    }
}

