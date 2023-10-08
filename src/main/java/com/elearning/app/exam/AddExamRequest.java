package com.elearning.app.exam;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AddExamRequest {
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
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
