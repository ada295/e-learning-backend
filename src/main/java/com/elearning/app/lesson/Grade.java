package com.elearning.app.lesson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Grade {

    @Id
    @SequenceGenerator(name = "grade_id_gen", sequenceName = "grade_id_seq",  initialValue = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "grade_id_gen")
    private Long id;
    private Double value;
    private String category;
    @OneToOne
    @JoinColumn(name = "task_student_id")
    @JsonIgnore
    private TaskStudent taskStudent;
    private String comment;

    public Grade() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public TaskStudent getTaskStudent() {
        return taskStudent;
    }

    public void setTaskStudent(TaskStudent taskStudent) {
        this.taskStudent = taskStudent;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
