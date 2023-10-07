package com.elearning.app.lesson;

import com.elearning.app.user.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "TaskStudent_gen", sequenceName = "TaskStudent_gen", initialValue = 50)
public class TaskStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TaskStudent_gen")
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private TaskStudentStatus status;
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonIgnore
    private Task task;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnore
    private UserAccount owner;
    private String filename;
    @OneToOne(mappedBy = "taskStudent")
    private Grade grade;

    public TaskStudent() {
    }
}
