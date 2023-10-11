package com.elearning.app.user;

import com.elearning.app.course.Course;
import com.elearning.app.exam.ExamResult;
import com.elearning.app.lesson.Grade;
import com.elearning.app.lesson.TaskStudent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "USER_ACCOUNT")
@Getter
@Setter
public class UserAccount {
    @Id
    @SequenceGenerator(name = "user_id_gen", sequenceName = "user_id_seq", initialValue = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_id_gen")
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role")
    @Column(name = "role")
    @Fetch(FetchMode.SUBSELECT)
    private Set<UserRole> roles;
    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Course> courses;
    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<TaskStudent> tasksStudent;
    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private Set<Grade> grades;
    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<ExamResult> myExamResults;
    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    private List<ExamResult> examResultsToCheck;

    @ManyToMany
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "courses_as_student_id"))
    @JsonIgnore
    private Set<Course> coursesAsStudent = new HashSet<>();
}
