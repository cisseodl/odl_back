package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Evaluations;
import com.odc.aws_learning.app.entity.Questions;
import com.odc.aws_learning.app.repository.EvaluationsRepository;
import com.odc.aws_learning.app.repository.QuestionsRepository;
import com.odc.aws_learning.app.repository.ReponsesRepository;
import com.odc.aws_learning.app.wrapper.Quiz_Answer;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationsService {
    private final EvaluationsRepository evaluationsRepository;
    private  final QuestionsRepository questionsRepository;
    private  final ReponsesRepository reponsesRepository;

    public EvaluationsService(EvaluationsRepository evaluationsRepository, QuestionsRepository questionsRepository, ReponsesRepository reponsesRepository)
    {
        this.evaluationsRepository = evaluationsRepository;
        this.questionsRepository = questionsRepository;
        this.reponsesRepository = reponsesRepository;
    }
    public CResponse<?> saveEvaluations(Evaluations evaluations) {
        try {
            Evaluations evaluations1 = evaluationsRepository.save(evaluations);
            return CResponse.success(evaluations1, "Evalution enregistré avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur d'enregistrement");
        }
    }

    public CResponse<?> getAll() {
        try {
            List<Evaluations> evaluations = evaluationsRepository.findAll();
            return CResponse.success(evaluations, "Les evaluations");
        } catch (Exception e) {
            return CResponse.error("Erreur de récupération");
        }
    }
    public CResponse<?> createEvaluation(Quiz_Answer quiz_answer){
        try {
            Evaluations evaluation = new Evaluations();
            evaluation.setTitle(quiz_answer.getEvaluationTitle()
            );
            Evaluations evaliuationSave = evaluationsRepository.save(evaluation);
            quiz_answer.getQuestionsList().forEach(quiz -> {
                Questions question = new Questions();
                question.setTitle(quiz.getTitle());
                question.setDescription(quiz.getDescription());
                question.setStatus(quiz.getStatus());
                question.setImagePath(quiz.getImagePath());
                question.setType(quiz.getType());
                quiz.setEvaluations(evaliuationSave);
                Questions questionSave = questionsRepository.save(quiz);
                quiz.getReponses().forEach(answerR ->{
                    answerR.setQuestions(questionSave);
                    reponsesRepository.save(answerR);
                });
            });

            return CResponse.success(evaliuationSave, "Evaluation enregistré avec succes");

        }catch (Exception e){
            return CResponse.error("Erreur d'enregistrement");


        }
    }
}
