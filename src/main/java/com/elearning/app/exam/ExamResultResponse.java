package com.elearning.app.exam;

import com.elearning.app.question.QuestionStudentAnswerResponse;
import com.elearning.app.user.UserAccount;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamResultResponse {
    private Long examResultId;
    private List<QuestionStudentAnswerResponse> questions;
    private Exam exam;
    private UserAccount student;
    private Double points;
    private Double maxPoints;
    private String status;
}
