package com.elearning.app.responses.examdetails;

import com.elearning.app.question.QuestionType;

import java.util.List;

public class ExamDetailsQuestionResponse {
    private Long id;
    private String content;
    private int points;
    private String questionType;
    private List<ExamDetailsAnswerResponse> answers;

    public List<ExamDetailsAnswerResponse> getAnswers() {
        return answers;
    }

    public void setAnswers(List<ExamDetailsAnswerResponse> answers) {
        this.answers = answers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
}
