package com.elearning.app;

import com.elearning.app.lesson.Lesson;
import jakarta.persistence.*;

@Entity
@SequenceGenerator(name = "calendar_event_id_gen", sequenceName = "calendar_event_id_gen", initialValue = 50)
public class CalendarEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "calendar_event_id_gen")
    private Long id;
    private Integer day;
    private Integer month;
    private Integer year;
    private String description;
    private String type;
    private Integer hour;
    private Integer minutes;
    @ManyToOne
    private Lesson lesson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}