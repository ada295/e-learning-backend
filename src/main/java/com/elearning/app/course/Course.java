package com.elearning.app.course;

import com.elearning.app.announcement.Announcement;
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
    private String accessCode;
    private boolean finished;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<Announcement> announcements;

    //    private List<Student> students;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserAccount owner;

    public Course() {

    }
}
