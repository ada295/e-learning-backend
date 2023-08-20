package com.elearning.app.lesson;

import com.elearning.app.user.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class TaskStudent {

    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private TaskStudentStatus status;
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonIgnore
    private Task task;
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnore
    private Student student;
    private Long points;

    public TaskStudent() {
    }

    public TaskStudentStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStudentStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }
}
