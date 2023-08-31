package com.elearning.app.exam;

import com.elearning.app.course.Course;
import com.elearning.app.question.Question;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Exam {

    @Id
    @SequenceGenerator(name = "ExamSequenceForId", sequenceName = "exam_id_seq", initialValue = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ExamSequenceForId")
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxMinutes;
    @OneToMany(mappedBy = "exam")
    @JsonIgnore
    private List<Question> questions;
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getMaxMinutes() {
        return maxMinutes;
    }

    public void setMaxMinutes(Integer maxMinutes) {
        this.maxMinutes = maxMinutes;
    }
}
