package com.elearning.app.lesson;

import com.elearning.app.course.Course;
import com.elearning.app.user.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Grade {

    @Id
    @SequenceGenerator(name = "grade_id_gen", sequenceName = "grade_id_seq", initialValue = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "grade_id_gen")
    private Long id;
    private Double value;
    private String category;
    @OneToOne
    @JoinColumn(name = "task_owner_id")
    @JsonIgnore
    private TaskStudent taskStudent;
    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private UserAccount student;
    @ManyToMany
    @JoinTable(
            name = "courses_grades",
            joinColumns = @JoinColumn(name = "grade_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();
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

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public UserAccount getStudent() {
        return student;
    }

    public void setStudent(UserAccount student) {
        this.student = student;
    }
}
