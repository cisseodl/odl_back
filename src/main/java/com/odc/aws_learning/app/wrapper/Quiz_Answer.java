package com.odc.aws_learning.app.wrapper;

import com.odc.aws_learning.app.entity.Questions;

import java.util.List;

public class Quiz_Answer {
   public String evaluationTitle;
    public List<Questions> questionsList;


    public String getEvaluationTitle() {
        return evaluationTitle;
    }

    public void setEvaluationTitle(String evaluationTitle) {
        this.evaluationTitle = evaluationTitle;
    }

    public List<Questions> getQuestionsList() {
        return questionsList;
    }

    public void setQuestionsList(List<Questions> questionsList) {
        this.questionsList = questionsList;
    }
}


