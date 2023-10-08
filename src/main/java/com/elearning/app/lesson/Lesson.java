package com.elearning.app.lesson;

import com.elearning.app.CalendarEvent;
import com.elearning.app.course.Course;
import com.elearning.app.exam.Exam;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@SequenceGenerator(name = "lesson_id_gen", sequenceName = "lesson_id_gen", initialValue = 50)
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "lesson_id_gen")
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "lesson")
    @JsonIgnore
    private Set<Grade> grades = new HashSet<>();

    @OneToMany
    private List<CalendarEvent> calendarEvents;

    @OneToMany
    private List<Material> materials;

    @OneToOne(mappedBy = "lesson")
    @JsonIgnore
    private Exam exam;

    public Lesson() {

    }

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

    public List<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    public void setCalendarEvents(List<CalendarEvent> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Set<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }
}
