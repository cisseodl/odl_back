package com.odc.aws_learning.app.service;


import com.odc.aws_learning.app.entity.Questions;
import com.odc.aws_learning.app.repository.QuestionsRepository;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class QuestionsService {
    private final QuestionsRepository questionsRepository;

    public QuestionsService(QuestionsRepository questionsRepository){
        this.questionsRepository = questionsRepository;
    }
    public CResponse<?> saveQuestions(Questions questions){
        try {
            Questions questions1 = questionsRepository.save(questions);
            return CResponse.success(questions1,"Questions enregistré avec succès");
        }
        catch (Exception e){
            return CResponse.error("Erreur d'enregistrement");

        }
    }
    public CResponse<?> getAll(){
        try {
            List<Questions> questions = questionsRepository.findAll();
            return CResponse.success(questions, "Les Questions");

        }catch (Exception e){
            return CResponse.error("Erreur de récupération");

        }
    }
}
