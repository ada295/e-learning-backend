package com.elearning.app.course;

import com.elearning.app.exam.Exam;
import com.elearning.app.lesson.Lesson;
import com.elearning.app.user.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "course_id_gen", sequenceName = "course_id_gen", initialValue = 50)
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

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserAccount owner;

    public Course() {

    }

    public Course(Long id, String name, String description, boolean finished) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.finished = finished;
    }
}
