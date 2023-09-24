package com.elearning.app.question;

import com.elearning.app.answer.Answer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionStudentAnswerResponse {
    private Question question;
    private List<Answer> questionAnswers;
    private List<Answer> studentAnswers;
    private String openQuestionAnswer;
    private Double studentPoints;
}
