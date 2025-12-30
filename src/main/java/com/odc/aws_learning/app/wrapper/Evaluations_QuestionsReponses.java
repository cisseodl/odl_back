package com.odc.aws_learning.app.wrapper;

import java.util.List;

public class Evaluations_QuestionsReponses {
    List<Question_Reponses> question_reponses;
    Long evaluationId;


    public List<Question_Reponses> getQuestion_reponses() {
        return question_reponses;
    }

    public void setQuestion_reponses(List<Question_Reponses> question_reponses) {
        this.question_reponses = question_reponses;
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }


}
