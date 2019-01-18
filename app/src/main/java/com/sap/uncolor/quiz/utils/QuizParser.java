package com.sap.uncolor.quiz.utils;

import com.sap.uncolor.quiz.models.Question;
import com.sap.uncolor.quiz.models.Quiz;

public class QuizParser {

    public static Quiz toQuizModel(Object[][] responseObject){
        Quiz quiz = new Quiz();
        Question question1 = new Question();
        question1.setVariant1((String) responseObject[0][0]);
        question1.setVariant2((String) responseObject[0][1]);
        question1.setVariant3((String) responseObject[0][2]);
        question1.setVariant4((String) responseObject[0][3]);
        question1.setQuestion((String) responseObject[0][4]);
        question1.setVariantRight((Double) responseObject[0][5]);

        Question question2 = new Question();
        question2.setVariant1((String) responseObject[1][0]);
        question2.setVariant2((String) responseObject[1][1]);
        question2.setVariant3((String) responseObject[1][2]);
        question2.setVariant4((String) responseObject[1][3]);
        question2.setQuestion((String) responseObject[1][4]);
        question2.setVariantRight((Double) responseObject[1][5]);

        Question question3 = new Question();
        question3.setVariant1((String) responseObject[2][0]);
        question3.setVariant2((String) responseObject[2][1]);
        question3.setVariant3((String) responseObject[2][2]);
        question3.setVariant4((String) responseObject[2][3]);
        question3.setQuestion((String) responseObject[2][4]);
        question3.setVariantRight((Double) responseObject[2][5]);

        quiz.add(question1);
        quiz.add(question2);
        quiz.add(question3);

        return quiz;
    }
}
