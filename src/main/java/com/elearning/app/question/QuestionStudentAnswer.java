package com.elearning.app.question;

import com.elearning.app.answer.Answer;
import com.elearning.app.exam.ExamResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class QuestionStudentAnswer {
    @Id
    @SequenceGenerator(name = "QuestionStudentAnswerSequenceForId", sequenceName = "question_student_answer_id_seq", initialValue = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "QuestionStudentAnswerSequenceForId")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore
    private Question question;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "exam_result_id")
    @JsonIgnore
    private ExamResult examResult;
    @ManyToMany(mappedBy = "questionStudentAnswers", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Answer> studentAnswers = new ArrayList<>();
    private String openQuestionAnswer;
    private Double openQuestionAnswerPoints;
}
