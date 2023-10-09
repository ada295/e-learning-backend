package com.elearning.app.responses;

import com.elearning.app.lesson.Grade;
import com.elearning.app.lesson.TaskStudent;
import com.elearning.app.lesson.TaskToDo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeResp {
    private Grade grade;
    private TaskToDo task;
}
