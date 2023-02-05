package com.elearning.app.responses.examdetails;

import java.util.List;

public class ExamDetailsResponse {
    private ExamDetailsExamResponse exam;
    private List<ExamDetailsQuestionResponse> questions;


    public ExamDetailsExamResponse getExam() {
        return exam;
    }

    public void setExam(ExamDetailsExamResponse exam) {
        this.exam = exam;
    }

    public List<ExamDetailsQuestionResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ExamDetailsQuestionResponse> questions) {
        this.questions = questions;
    }
}


