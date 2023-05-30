package com.elearning.app.exam;

public class AddExamQuestionAnswerRequest {
    private String content;
    private Boolean correct;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
