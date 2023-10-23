package com.elearning.app.responses.coursedetails;

import com.elearning.app.user.UserAccount;

public class CourseDetailsCourseResponse {
    private Long id;
    private String name;
    private String description;
    private String accessCode;
    private UserAccount owner;
    private boolean finished;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public UserAccount getOwner() {
        return owner;
    }

    public void setOwner(UserAccount owner) {
        this.owner = owner;
    }
}
