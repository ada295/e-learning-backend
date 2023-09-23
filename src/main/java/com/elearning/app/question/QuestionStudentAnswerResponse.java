package com.elearning.app.question;

import com.elearning.app.answer.Answer;
import com.elearning.app.exam.ExamResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
public class QuestionStudentAnswerResponse {
    private Question question;
    private List<Answer> studentAnswers;
    private String openQuestionAnswer;
    private Double openQuestionAnswerPoints;
}
