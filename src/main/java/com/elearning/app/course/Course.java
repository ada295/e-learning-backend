package com.elearning.app.course;

import com.elearning.app.exam.Exam;
import com.elearning.app.lesson.Lesson;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@SequenceGenerator(name = "course_id_gen", sequenceName = "course_id_gen",  initialValue = 50)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "course_id_gen")
    private Long id;
    private String name;
    private String description;
    private boolean finished;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Lesson> lessons;

//    private List<Student> students;
    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Exam> exams;

    public Course() {

    }

    public Course(Long id, String name, String description, boolean finished) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.finished = finished;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
//                ", lessons=" + lessons +
//                ", students=" + students +
                '}';
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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

//    public List<Student> getStudents() {
//        return students;
//    }
//
//    public void setStudents(List<Student> students) {
//        this.students = students;
//    }
}
