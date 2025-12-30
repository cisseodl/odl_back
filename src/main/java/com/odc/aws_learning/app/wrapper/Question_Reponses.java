package com.odc.aws_learning.app.wrapper;

import com.odc.aws_learning.app.entity.Questions;
import com.odc.aws_learning.app.entity.Reponses;

import java.util.List;

public class Question_Reponses {
    public Questions getQuestion() {
        return question;
    }

    public void setQuestion(Questions question) {
        this.question = question;
    }

    public List<Reponses> getReponses() {
        return reponses;
    }

    public void setReponses(List<Reponses> reponses) {
        this.reponses = reponses;
    }

    Questions question;
    List<Reponses> reponses ;
}
