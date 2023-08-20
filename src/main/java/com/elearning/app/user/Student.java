package com.elearning.app.user;

import com.elearning.app.lesson.TaskStudent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@SequenceGenerator(name = "StudentSequenceForId", sequenceName = "student_id_seq",  initialValue = 50)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "StudentSequenceForId")
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String pesel;
    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<TaskStudent> tasksStudent;

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public List<TaskStudent> getTasksStudent() {
        return tasksStudent;
    }

    public void setTasksStudent(List<TaskStudent> tasksStudent) {
        this.tasksStudent = tasksStudent;
    }
}
