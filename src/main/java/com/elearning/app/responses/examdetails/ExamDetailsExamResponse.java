package com.elearning.app.responses.examdetails;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExamDetailsExamResponse {
    private Long id;
    private String name;
    private Integer maxMinutes;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
