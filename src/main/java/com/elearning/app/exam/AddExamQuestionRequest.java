package com.elearning.app.exam;

import java.util.List;

public class AddExamQuestionRequest {
    private String content;
    private Integer points;
    private String type;
    private List<AddExamQuestionAnswerRequest> answers;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<AddExamQuestionAnswerRequest> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AddExamQuestionAnswerRequest> answers) {
        this.answers = answers;
    }
}
