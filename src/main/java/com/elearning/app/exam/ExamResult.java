package com.elearning.app.exam;

import com.elearning.app.question.QuestionStudentAnswer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class ExamResult {

    @Id
    @SequenceGenerator(name = "ExamResultGenId", sequenceName = "exam_result_id_seq", initialValue = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ExamResultGenId")
    private Long id;
    @OneToMany(mappedBy = "examResult", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<QuestionStudentAnswer> studentAnswers;
    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;
}
