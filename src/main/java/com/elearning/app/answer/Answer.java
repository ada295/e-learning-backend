package com.elearning.app.answer;

import com.elearning.app.question.Question;
import com.elearning.app.question.QuestionStudentAnswer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Answer {

    @Id
    @SequenceGenerator(name = "AnswerSequenceForId", sequenceName = "answer_id_seq",  initialValue = 50)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "AnswerSequenceForId")
    private Long id;
    private String content;
    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore
    private Question question;

    @ManyToMany
    @JoinTable(
            name = "answer_student_answer",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "question_student_answer_id"))
    @JsonIgnore
    private List<QuestionStudentAnswer> questionStudentAnswers;

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

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
