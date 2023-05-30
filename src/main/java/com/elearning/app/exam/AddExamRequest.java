package com.elearning.app.exam;

import java.util.List;

public class AddExamRequest {
    private String name;
    private String description;
    private List<AddExamQuestionRequest> questions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AddExamQuestionRequest> getQuestions() {
        return questions;
    }

    public void setQuestions(List<AddExamQuestionRequest> questions) {
        this.questions = questions;
    }
}
