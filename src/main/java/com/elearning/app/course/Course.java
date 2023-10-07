package com.elearning.app.course;

import com.elearning.app.announcement.Announcement;
import com.elearning.app.lesson.Lesson;
import com.elearning.app.user.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(mappedBy = "coursesAsStudent", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<UserAccount> students = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserAccount owner;

    public Course() {

    }
}
