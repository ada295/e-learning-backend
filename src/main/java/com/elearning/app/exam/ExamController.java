package com.elearning.app.exam;


import com.elearning.app.answer.Answer;
import com.elearning.app.question.Question;
import com.elearning.app.question.QuestionType;
import com.elearning.app.responses.examdetails.ExamDetailsAnswerResponse;
import com.elearning.app.responses.examdetails.ExamDetailsExamResponse;
import com.elearning.app.responses.examdetails.ExamDetailsQuestionResponse;
import com.elearning.app.responses.examdetails.ExamDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ExamController {

    @Autowired
    private ExamRepository repository;

//    @GetMapping("/exam")
//    public List<ExamDetailsExamResponse> getExams () {
//        return repository.findAll();
//    }

    @GetMapping("/exam/{id}")
    public ExamDetailsResponse getExam(@PathVariable Long id) {
        Optional<Exam> optionalExam = repository.findById(id);

        if (optionalExam.isPresent()) {
            //odpowiedz zawierajaca wszystkie dane wymagane przez ExamDetails

            ExamDetailsResponse response = new ExamDetailsResponse();

            //Id i nazwa testu
            ExamDetailsExamResponse examResponse = new ExamDetailsExamResponse();
            examResponse.setId(optionalExam.get().getId());
            examResponse.setName(optionalExam.get().getName());

            //lista pytań
            List<Question> questions = optionalExam.get().getQuestions();
            List<ExamDetailsQuestionResponse> questionResponses = new ArrayList<>();

            for (Question question : questions) {
                ExamDetailsQuestionResponse questionResponse = new ExamDetailsQuestionResponse();
                questionResponse.setId(question.getId());
                questionResponse.setContent(question.getContent());
                questionResponse.setPoints(question.getPoints());
                questionResponse.setQuestionType(question.getQuestionType().name());

                //lista odpowiedzi
                List<Answer> answers = question.getAnswers();
                List<ExamDetailsAnswerResponse> answerResponses = new ArrayList<>();

                for (Answer answer : answers) {
                    ExamDetailsAnswerResponse answerResponse = new ExamDetailsAnswerResponse();
                    answerResponse.setId(answer.getId());
                    answerResponse.setContent(answer.getContent());
                    answerResponses.add(answerResponse);
                }

                questionResponse.setAnswers(answerResponses);

                questionResponses.add(questionResponse);
            }


            response.setExam(examResponse);
            response.setQuestions(questionResponses);

            return response;
        }

        return null;
    }

    // zabezpieczyc przed nieprawidlowym id exam
    @PostMapping("/exam/{id}/finish")
    public void finishExam (@PathVariable Long id, @RequestBody List<ExamFinishRequest> body) {
        Double points = 0.0;
        for (ExamFinishRequest examFinishRequest : body) {
            Long questionId = examFinishRequest.getQuestionId();
            Exam exam = repository.findById(id).get();
            Question questionFromDB = exam.getQuestions().stream().filter(question -> question.getId().equals(questionId))
                    .findFirst().get();

            if(questionFromDB.getQuestionType() == QuestionType.ONE_CHOICE) {
                Long chosenAnswerId = (Long) examFinishRequest.getAnswers();
                Long correctAnswer = questionFromDB.getAnswers().stream().filter(answer -> answer.isCorrect())
                        .findFirst().get().getId();
                if (chosenAnswerId.equals(correctAnswer)) {
                    points += questionFromDB.getPoints();
                }
            }

        }


        System.out.println(body);
    }
}
