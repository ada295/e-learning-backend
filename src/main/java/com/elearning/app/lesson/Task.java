package com.elearning.app.lesson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import java.util.Date;
import java.util.List;
@SequenceGenerator(name = "task_id_gen", sequenceName = "task_id_gen", initialValue = 50)

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "task_id_gen")
    private Long id;
    private String description;
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
    @OneToMany(mappedBy = "task")
    @JsonIgnore
    private List<TaskStudent> taskStudents;

    public Task() {

    }

    public Task(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public List<TaskStudent> getTaskStudents() {
        return taskStudents;
    }

    public void setTaskStudents(List<TaskStudent> taskStudents) {
        this.taskStudents = taskStudents;
    }
}
