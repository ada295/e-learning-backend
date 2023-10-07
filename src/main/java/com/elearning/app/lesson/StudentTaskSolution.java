package com.elearning.app.lesson;

import com.elearning.app.responses.coursedetails.CourseDetailsStudentResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentTaskSolution {
    private TaskToDo task;
    private CourseDetailsStudentResponse student;
}
