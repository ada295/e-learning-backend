package com.elearning.app.exam;

import com.elearning.app.question.QuestionType;

import java.util.List;

public class ExamFinishRequest {
    private Long questionId;
    private List<Long> answersIds;
    private Object answers;
    private QuestionType questionType;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public List<Long> getAnswersIds() {
        return answersIds;
    }

    public void setAnswersIds(List<Long> answersIds) {
        this.answersIds = answersIds;
    }

    public Object getAnswers() {
        return answers;
    }

    public void setAnswers(Object answers) {
        this.answers = answers;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }
}
