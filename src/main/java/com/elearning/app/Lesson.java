package com.elearning.app;

import jakarta.persistence.*;

@Entity
public class Lesson {
    @Id
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name="course_id", nullable=false)
    private Course course;

    public Lesson(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", name='" + name + '\'' +
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
