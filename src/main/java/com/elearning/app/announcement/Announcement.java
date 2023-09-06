package com.elearning.app.announcement;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

public class Announcement {

    @Id
    @SequenceGenerator(name = "AnnouncementSequenceForId", sequenceName = "announcement_id_seq",  initialValue = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "AnnouncementSequenceForId")
    private Long id;
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
