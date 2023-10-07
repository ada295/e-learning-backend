package com.elearning.app.announcement;

import com.elearning.app.course.Course;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
public class Announcement {

    @Id
    @SequenceGenerator(name = "AnnouncementSequenceForId", sequenceName = "announcement_id_seq",  initialValue = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "AnnouncementSequenceForId")
    private Long id;
    private String name;
    private String description;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String content) {
        this.description = content;
    }
}
