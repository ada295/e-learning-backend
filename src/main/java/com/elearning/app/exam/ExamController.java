package com.elearning.app.exam;


import com.elearning.app.answer.Answer;
import com.elearning.app.answer.AnswerRepository;
import com.elearning.app.course.CourseRepository;
import com.elearning.app.lesson.Lesson;
import com.elearning.app.lesson.LessonRepository;
import com.elearning.app.question.Question;
import com.elearning.app.question.QuestionRepository;
import com.elearning.app.question.QuestionType;
import com.elearning.app.responses.examdetails.ExamDetailsAnswerResponse;
import com.elearning.app.responses.examdetails.ExamDetailsExamResponse;
import com.elearning.app.responses.examdetails.ExamDetailsQuestionResponse;
import com.elearning.app.responses.examdetails.ExamDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ExamController {

    @Autowired
    private ExamRepository repository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

//    @GetMapping("/exam")
//    public List<ExamDetailsExamResponse> getExams () {
//        return repository.findAll();
//    }

    @GetMapping("/exam/{id}/active-exam")
    public ExamDetailsResponse getActiveExamWithQuestions(@PathVariable Long id) {
        Optional<Exam> optionalExam = repository.findById(id);

        if (optionalExam.isPresent()) {
            //odpowiedz zawierajaca wszystkie dane wymagane przez ExamDetails

            ExamDetailsResponse response = new ExamDetailsResponse();

            //Id i nazwa testu
            ExamDetailsExamResponse examResponse = getExamDetailsExamResponse(optionalExam.get());

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

    @GetMapping("/lesson/{id}/details")
    public ExamDetailsResponse getExamDetails(@PathVariable Long id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);

        if (lesson.isPresent() && lesson.get().getExam() != null) {
            //odpowiedz zawierajaca wszystkie dane wymagane przez ExamDetails

            ExamDetailsResponse response = new ExamDetailsResponse();
            ExamDetailsExamResponse examResponse = getExamDetailsExamResponse(lesson.get().getExam());

            response.setExam(examResponse);

            return response;
        }

        return null;
    }

    private ExamDetailsExamResponse getExamDetailsExamResponse(Exam exam) {
        //Id i nazwa testu
        ExamDetailsExamResponse examResponse = new ExamDetailsExamResponse();
        examResponse.setId(exam.getId());
        examResponse.setName(exam.getName());
        examResponse.setStartDate(exam.getStartDate());
        examResponse.setEndDate(exam.getEndDate());
        return examResponse;
    }

    @PostMapping("/lesson/{lessonId}/exam")
    public void addExam(@PathVariable Long lessonId, @RequestBody AddExamRequest request) {
        Exam exam = new Exam();
        exam.setName(request.getName());
        exam.setDescription(request.getDescription());
        Optional<Lesson> byId = lessonRepository.findById(lessonId);
        exam.setLesson(byId.get());
        exam = repository.save(exam);
        for (AddExamQuestionRequest requestQuestion : request.getQuestions()) {
            Question question = new Question();
            question.setContent(requestQuestion.getContent());
            question.setQuestionType(QuestionType.valueOf(requestQuestion.getType()));
            question.setPoints(requestQuestion.getPoints());
            question.setExam(exam);
            question = questionRepository.save(question);
            for (AddExamQuestionAnswerRequest requestAnswer : requestQuestion.getAnswers()) {
                Answer answer = new Answer();
                answer.setContent(requestAnswer.getContent());
                answer.setCorrect(requestAnswer.getCorrect());
                answer.setQuestion(question);
                answerRepository.save(answer);
            }
        }
    }

    // zabezpieczyc przed nieprawidlowym id exam
    @PostMapping("/exam/{id}/finish")
    public void finishExam(@PathVariable Long id, @RequestBody List<ExamFinishRequest> body) {
        Double points = 0.0;
        for (ExamFinishRequest examFinishRequest : body) {
            Long questionId = examFinishRequest.getQuestionId();
            Exam exam = repository.findById(id).get();
            Question questionFromDB = exam.getQuestions().stream().filter(question -> question.getId().equals(questionId))
                    .findFirst().get();

            if (questionFromDB.getQuestionType() == QuestionType.ONE_CHOICE) {
                Long chosenAnswerId = Long.parseLong(examFinishRequest.getAnswers().toString());
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
