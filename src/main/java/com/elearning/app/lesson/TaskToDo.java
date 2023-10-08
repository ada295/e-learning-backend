package com.elearning.app.lesson;

import com.elearning.app.user.UserAccount;

public class TaskToDo {
    private Task task;
    private TaskStudent taskStudent;
    private TaskStudentStatus status;
    private String icon;
    private UserAccount student;
    private Double grade;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public TaskStudent getTaskStudent() {
        return taskStudent;
    }

    public void setTaskStudent(TaskStudent taskStudent) {
        this.taskStudent = taskStudent;
    }

    public TaskStudentStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStudentStatus status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public UserAccount getStudent() {
        return student;
    }

    public void setStudent(UserAccount student) {
        this.student = student;
    }
}
