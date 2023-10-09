package com.elearning.app.responses;

import com.elearning.app.lesson.Grade;
import com.elearning.app.responses.coursedetails.CourseDetailsCourseResponse;
import com.elearning.app.responses.coursedetails.CourseDetailsStudentResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GradebookResponse {
    private CourseDetailsCourseResponse course;
    private CourseDetailsStudentResponse student;
    private List<GradeResp> grades;
    private Double avg;
}
