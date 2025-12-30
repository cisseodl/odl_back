package com.odc.aws_learning.app.service;

import com.odc.aws_learning.app.entity.Answer;
import com.odc.aws_learning.app.entity.Evaluations;
import com.odc.aws_learning.app.entity.InfoTest;
import com.odc.aws_learning.app.repository.*;
import com.odc.aws_learning.app.wrapper.Evaluations_QuestionsReponses;
import com.odc.aws_learning.auth.base.response.CResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionsRepository questionsRepository;
    private final ReponsesRepository reponsesRepository;
    private final EvaluationsRepository evaluationsRepository;
    private final InfotestRepository infotestRepository;

    public AnswerService(AnswerRepository answerRepository, QuestionsRepository questionsRepository, ReponsesRepository reponsesRepository, EvaluationsRepository evaluationsRepository, InfotestRepository infotestRepository) {
        this.answerRepository = answerRepository;
        this.questionsRepository = questionsRepository;
        this.reponsesRepository = reponsesRepository;
        this.evaluationsRepository = evaluationsRepository;
        this.infotestRepository = infotestRepository;
    }

    public CResponse<?> saveAnswer(Answer answer) {
        try {
            Answer answer1 = answerRepository.save(answer);
            return CResponse.success(answer1, "Answer enregistré avec succès");
        } catch (Exception e) {
            return CResponse.error("Erreur d'enregistrement");
        }
    }

    public CResponse<?> getAll() {
        try {
            List<Answer> answers = answerRepository.findAll();
            return CResponse.success(answers, "Les answers");
        } catch (Exception e) {
            return CResponse.error("Erreur de récupération");
        }
    }

    public CResponse<?> saveLearnerTest(Evaluations_QuestionsReponses evaluations_questionsReponses) {
        try {

            Optional<Evaluations> evaluationsOptional = evaluationsRepository.findById(evaluations_questionsReponses.getEvaluationId());
            if (evaluationsOptional.isPresent()) {
                Evaluations evaluations = evaluationsOptional.get();

                InfoTest infoTest = new InfoTest();
                infoTest.setEvaluations(evaluations);
                InfoTest infoTestSave = infotestRepository.save(infoTest);
                List<Answer> answerList = new ArrayList<>();

                evaluations_questionsReponses.getQuestion_reponses().forEach(question_reponses -> {
                    question_reponses.getReponses().forEach(reponses -> {
                        Answer answer = new Answer();
                        answer.setInfoTest(infoTestSave);
                        answer.setQuestion(question_reponses.getQuestion());
                        answer.setReponse(reponses);

                        answerList.add(answer);

                    });
                });
                List<Answer> answers = answerRepository.saveAll(answerList);
                return CResponse.success(answers.size(), "Votre Test a été soumi avec succes!");
            } else {
                return CResponse.error("Cette evaluation est introuvable");
            }

        } catch (Exception e) {
            return CResponse.error("Erreur d'enregistrement");

        }
    }
}




