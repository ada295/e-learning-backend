package com.elearning.app.responses.examdetails;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExamDetailsExamResponse {
    private Long id;
    private String name;
    private Integer maxMinutes;
    private LocalDate startDate;
    private LocalDate endDate;
}
