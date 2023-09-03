package com.elearning.app.question;

import com.elearning.app.answer.Answer;
import com.elearning.app.exam.Exam;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import java.util.List;

@Entity
public class Question {
    @Id
    @SequenceGenerator(name = "QuestionSequenceForId", sequenceName = "question_id_seq",  initialValue = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "QuestionSequenceForId")
    private Long id;
    private String content;
    private Integer points;
    @Enumerated(value = EnumType.STRING)
    private QuestionType questionType;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    @JsonIgnore
    private Exam exam;

    @OneToMany(mappedBy = "question")
    @JsonIgnore
    private List<Answer> answers;

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

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }
}
