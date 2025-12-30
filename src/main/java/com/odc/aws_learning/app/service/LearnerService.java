package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Chapter;
import com.odc.aws_learning.app.entity.LearnerChapter;
import com.odc.aws_learning.app.repository.ChapterRepository;
import com.odc.aws_learning.app.repository.LearnerChapterRepository;
import com.odc.aws_learning.app.wrapper.ValidateChapter;
import com.odc.aws_learning.auth.entities.User;
import com.odc.aws_learning.auth.repository.UserRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LearnerService {

    private final ChapterRepository chapterRepository;
    private final UserRepository userRepository;
    private final LearnerChapterRepository learnerChapterRepository;

    public LearnerService(ChapterRepository chapterRepository, UserRepository userRepository, LearnerChapterRepository learnerChapterRepository) {
        this.chapterRepository = chapterRepository;
        this.userRepository = userRepository;
        this.learnerChapterRepository = learnerChapterRepository;
    }

    public CResponse<?> saveLearner(ValidateChapter validateChapter){
        try {
            // Vérifier si l'utilisateur existe
            Optional<User> optionalUser = userRepository.findById(validateChapter.getUserId());
            if(optionalUser.isPresent()){
                Optional<Chapter> optionalChapter = chapterRepository.findByActivateAndIdAndCourseId(true, validateChapter.getChapitreId(), validateChapter.getCoursId());
                if (optionalChapter.isPresent()) {
                    LearnerChapter learnerChapter = new LearnerChapter();
                    learnerChapter.setLearner(optionalUser.get());
                    learnerChapter.setChapter(optionalChapter.get());
                    LearnerChapter learnerChapter1 = learnerChapterRepository.save(learnerChapter);
                    return CResponse.success(learnerChapter1,"Enregistrer avec succès");
                }else {
                    return CResponse.error("Chapitre n'existe pas ");
                }
            } else {
                return CResponse.error("Users n'existe pas");
            }
        }catch (Exception e){
            return CResponse.error("");
        }

    }
}
