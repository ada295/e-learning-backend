package com.elearning.app.exam;


import com.elearning.app.answer.Answer;
import com.elearning.app.answer.AnswerRepository;
import com.elearning.app.course.Course;
import com.elearning.app.course.CourseRepository;
import com.elearning.app.lesson.Lesson;
import com.elearning.app.lesson.LessonRepository;
import com.elearning.app.question.*;
import com.elearning.app.responses.coursedetails.CourseDetailsCourseResponse;
import com.elearning.app.responses.examdetails.ExamDetailsAnswerResponse;
import com.elearning.app.responses.examdetails.ExamDetailsExamResponse;
import com.elearning.app.responses.examdetails.ExamDetailsQuestionResponse;
import com.elearning.app.responses.examdetails.ExamDetailsResponse;
import com.elearning.app.user.UserAccount;
import com.elearning.app.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ExamController {

    @Autowired
    private ExamRepository repository;
    @Autowired
    private ExamResultRepository examResultRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/exam/{lessonId}/active-exam")
    public ExamDetailsResponse getActiveExamWithQuestions(@PathVariable Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        Optional<Exam> optionalExam = Optional.of(lesson.getExam());

        if (optionalExam.isPresent()) {

            if(optionalExam.get().getEndDate().isBefore(LocalDateTime.now())
                    || optionalExam.get().getStartDate().isAfter(LocalDateTime.now())) {
                return null;
            }
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

    @GetMapping("/lesson/{id}/exam-details")
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
        examResponse.setDescription(exam.getDescription());
        examResponse.setStartDate(exam.getStartDate());
        examResponse.setEndDate(exam.getEndDate());
        return examResponse;
    }

    @PostMapping("/lesson/{lessonId}/exam")
    public void addExam(@PathVariable Long lessonId, @RequestBody AddExamRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(e -> e.getAuthority().equals("TEACHER"))) {
            return;
        }
        Exam exam = new Exam();
        exam.setName(request.getName());
        exam.setDescription(request.getDescription());
        exam.setStartDate(request.getStartDate());
        exam.setEndDate(request.getEndDate());
        Optional<Lesson> byId = lessonRepository.findById(lessonId);
        Course course = byId.get().getCourse();
        if (course.isFinished()) {
            throw new RuntimeException("Kurs Zakończony!");
        }
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

    @PutMapping("/lesson/{lessonId}/exam")
    public void editExam(@PathVariable Long lessonId, @RequestBody AddExamRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(e -> e.getAuthority().equals("TEACHER"))) {
            return;
        }
        Lesson lesson = lessonRepository.findById(lessonId).get();

        Course course = lesson.getCourse();
        if (course.isFinished()) {
            throw new RuntimeException("Kurs Zakończony!");
        }

        Exam exam = lesson.getExam();
        exam.setName(request.getName());
        exam.setDescription(request.getDescription());
        exam.setStartDate(request.getStartDate());
        exam.setEndDate(request.getEndDate());
        exam = repository.save(exam);
    }

    @PostMapping("/exam-result/{examResultId}/question/{questionId}")
    public ResponseEntity updatePointsForOpenQuestion(@PathVariable Long examResultId, @PathVariable Long questionId, @RequestBody Map<String, String> body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(e -> e.getAuthority().equals("TEACHER"))) {
            return ResponseEntity.status(403).build();
        }

        ExamResult examResult = examResultRepository.findById(examResultId).get();
        List<QuestionStudentAnswer> studentAnswers = examResult.getStudentAnswers();
        QuestionStudentAnswer questionStudentAnswer = studentAnswers.stream().filter(e -> e.getQuestion().getId().equals(questionId)).findFirst().get();
        Double points = Double.valueOf(body.get("points"));
        if (points < 0 || points > questionStudentAnswer.getQuestion().getPoints()) {
            return ResponseEntity.status(400).build();
        }
        questionStudentAnswer.setOpenQuestionAnswerPoints(points);
        examResultRepository.save(examResult);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/exam-result/{examResultId}")
    public ResponseEntity finishAndCloseExamResult(@PathVariable Long examResultId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(e -> e.getAuthority().equals("TEACHER"))) {
            return ResponseEntity.status(403).build();
        }

        ExamResult examResult = examResultRepository.findById(examResultId).get();

        Lesson lesson = examResult.getExam().getLesson();

        Course course = lesson.getCourse();
        if (course.isFinished()) {
            throw new RuntimeException("Kurs Zakończony!");
        }

        examResult.getStudentAnswers().stream().filter(e -> e.getQuestion().getQuestionType() == QuestionType.OPEN)
                .filter(e -> e.getOpenQuestionAnswerPoints() == null)
                .forEach(e -> e.setOpenQuestionAnswerPoints(0.0));
        examResult.setStatus("CHECKED_BY_TEACHER");
        examResultRepository.save(examResult);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/exam-result/{lessonId}")
    public ResponseEntity<ExamResultResponse> getResultExam(@PathVariable Long lessonId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(e -> e.getAuthority().equals("STUDENT"))) {
            return ResponseEntity.status(403).build();
        }

        UserAccount loggedUser = getUserAccount();
        ExamResult examResult = lessonRepository.findById(lessonId).get().getExam().getExamResults()
                .stream().filter(e -> e.getStudent().equals(loggedUser)).findFirst().get();
        return ResponseEntity.ok(buildExamResultResponse(examResult));
    }

    @GetMapping("/teacher-exam-results/{lessonId}")
    public ResponseEntity<List<ExamResultResponse>> getResultsExams(@PathVariable Long lessonId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(e -> e.getAuthority().equals("TEACHER"))) {
            return ResponseEntity.status(403).build();
        }

        List<ExamResultResponse> response = new ArrayList<>();

        UserAccount loggedUser = getUserAccount();
        List<ExamResult> examResults = lessonRepository.findById(lessonId).get().getExam().getExamResults()
                .stream().filter(e -> e.getTeacher().equals(loggedUser)).collect(Collectors.toList());
        for (ExamResult examResult : examResults) {
            ExamResultResponse examResultResponse = buildExamResultResponse(examResult);
            response.add(examResultResponse);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teacher-exam-results/{examResultId}/details")
    public ResponseEntity<ExamResultResponse> getResultExamDetailsForTeacher(@PathVariable Long examResultId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(e -> e.getAuthority().equals("TEACHER"))) {
            return ResponseEntity.status(403).build();
        }
        ExamResult examResult = examResultRepository.findById(examResultId).get();
        ExamResultResponse examResultResponse = buildExamResultResponse(examResult);
        return ResponseEntity.ok(examResultResponse);
    }

    @PostMapping("/exam/{id}/finish")
    public ResponseEntity<ExamResultResponse> finishExam(@PathVariable Long id, @RequestBody List<ExamFinishRequest> body) {
        UserAccount loggedUser = getUserAccount();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(e -> e.getAuthority().equals("STUDENT"))) {
            return ResponseEntity.status(403).build();
        }

        Optional<ExamResult> examResultOptional = lessonRepository.findById(id).get().getExam().getExamResults()
                .stream().filter(e -> e.getStudent().equals(loggedUser)).findFirst();

        if (examResultOptional.isPresent()) {
            return ResponseEntity.status(400).build();
        }

        ExamResult examResult = new ExamResult();
        Exam exam = lessonRepository.findById(id).get().getExam();

        Lesson lesson = exam.getLesson();

        Course course = lesson.getCourse();
        if (course.isFinished()) {
            throw new RuntimeException("Kurs Zakończony!");
        }

        if(exam.getEndDate().isBefore(LocalDateTime.now())
                || exam.getStartDate().isAfter(LocalDateTime.now())) {
            return ResponseEntity.status(400).build();
        }

        examResult.setExam(exam);
        examResult.setStudent(loggedUser);
        examResult.setTeacher(exam.getLesson().getCourse().getOwner());
        List<QuestionStudentAnswer> studentAnswers = new ArrayList<>();

        for (ExamFinishRequest examFinishRequest : body) {
            Long questionId = examFinishRequest.getQuestionId();
            QuestionStudentAnswer questionStudentAnswer = new QuestionStudentAnswer();
            studentAnswers.add(questionStudentAnswer);

            Question questionFromDB = exam.getQuestions().stream().filter(question -> question.getId().equals(questionId))
                    .findFirst().get();
            questionStudentAnswer.setQuestion(questionFromDB);
            if (questionFromDB.getQuestionType() == QuestionType.OPEN) {
                questionStudentAnswer.setOpenQuestionAnswer(examFinishRequest.getAnswers().toString());
            } else if (questionFromDB.getQuestionType() == QuestionType.ONE_CHOICE) {
                Object answers = examFinishRequest.getAnswers();
                if (answers != null && !answers.toString().equalsIgnoreCase("")) {
                    Long chosenAnswerId = Long.parseLong(answers.toString());
                    Answer answer = answerRepository.findById(chosenAnswerId).get();
                    questionStudentAnswer.setStudentAnswers(Arrays.asList(answer));
                    answer.getQuestionStudentAnswers().add(questionStudentAnswer);
                }
            } else if (questionFromDB.getQuestionType() == QuestionType.MULTI_CHOICE) {
                List<Long> answersIds = examFinishRequest.getAnswersIds();
                List<Answer> chosenAnswers = new ArrayList<>();
                for (int i = 0; i < answersIds.size(); i++) {
                    List answers = (List) examFinishRequest.getAnswers();
                    if (answers.get(i).equals(Boolean.TRUE)) {
                        chosenAnswers.add(answerRepository.findById(answersIds.get(i)).get());
                    }
                }
                for (Answer chosenAnswer : chosenAnswers) {
                    chosenAnswer.getQuestionStudentAnswers().add(questionStudentAnswer);
                }
                questionStudentAnswer.setStudentAnswers(chosenAnswers);
            }
            questionStudentAnswer.setExamResult(examResult);
        }
        examResult.setStudentAnswers(studentAnswers);

        ExamResult saved = examResultRepository.save(examResult);
        return ResponseEntity.ok(buildExamResultResponse(saved));
    }

    private UserAccount getUserAccount() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        return userAccount;
    }

    private ExamResultResponse buildExamResultResponse(ExamResult saved) {
        ExamResultResponse examResultResponse = new ExamResultResponse();
        examResultResponse.setDescription(saved.getExam().getDescription());
        examResultResponse.setExamResultId(saved.getId());
        examResultResponse.setLessonId(saved.getExam().getLesson().getId());
        examResultResponse.setExam(saved.getExam());
        Course course = saved.getExam().getLesson().getCourse();
        CourseDetailsCourseResponse courseRes = new CourseDetailsCourseResponse();
        courseRes.setId(course.getId());
        courseRes.setName(course.getName());
        courseRes.setDescription(course.getDescription());
        examResultResponse.setCourse(courseRes);
        examResultResponse.setPoints(sumPoints(saved));
        examResultResponse.setStatus(saved.getStatus());
        examResultResponse.setMaxPoints(sumMaxPoints(saved.getExam()));
        examResultResponse.setStudent(saved.getStudent());
        List<QuestionStudentAnswerResponse> answers = new ArrayList<>();
        for (QuestionStudentAnswer studentAnswer : saved.getStudentAnswers()) {
            QuestionStudentAnswerResponse answer = new QuestionStudentAnswerResponse();
            answer.setStudentAnswers(studentAnswer.getStudentAnswers());
            answer.setQuestion(studentAnswer.getQuestion());
            answer.setQuestionAnswers(studentAnswer.getQuestion().getAnswers());
            answer.setOpenQuestionAnswer(studentAnswer.getOpenQuestionAnswer());
            if (studentAnswer.getQuestion().getQuestionType() != QuestionType.OPEN) {
                Double closedPoints = calculateClosedQuestionPoints(studentAnswer, studentAnswer.getStudentAnswers(), studentAnswer.getQuestion().getQuestionType());
                answer.setStudentPoints(closedPoints);
            } else {
                answer.setStudentPoints(studentAnswer.getOpenQuestionAnswerPoints());
            }
            answers.add(answer);
        }
        examResultResponse.setQuestions(answers);


        return examResultResponse;
    }

    private Double sumMaxPoints(Exam exam) {
        Integer pointsRes = 0;
        List<Integer> points = exam.getQuestions().stream().map(Question::getPoints)
                .collect(Collectors.toList());
        for (Integer point : points) {
            pointsRes += point;
        }
        return pointsRes * 1.0;
    }

    private Double sumPoints(ExamResult saved) {
        Double points = 0.0;
        for (QuestionStudentAnswer studentAnswer : saved.getStudentAnswers()) {
            List<Answer> studentAnswers = studentAnswer.getStudentAnswers();
            QuestionType questionType = studentAnswer.getQuestion().getQuestionType();
            if (questionType == QuestionType.OPEN) {
                Double openQuestionAnswerPoints = studentAnswer.getOpenQuestionAnswerPoints();
                if (openQuestionAnswerPoints != null) {
                    points += openQuestionAnswerPoints;
                }
            } else {
                points += calculateClosedQuestionPoints(studentAnswer, studentAnswers, questionType);
            }
        }
        return points;
    }

    private Double calculateClosedQuestionPoints(QuestionStudentAnswer studentAnswer, List<Answer> studentAnswers, QuestionType questionType) {
        Integer questionPoints = studentAnswer.getQuestion().getPoints();
        if (questionType == QuestionType.ONE_CHOICE) {
            if (studentAnswers.size() != 0) {
                Answer answer = studentAnswer.getStudentAnswers().get(0);
                if (answer.isCorrect()) {
                    return questionPoints * 1.0;
                }
            }
        } else if (questionType == QuestionType.MULTI_CHOICE) {
            int notCorrectStudentAnswers = 0;
            List<Answer> correctAnswers = studentAnswer.getQuestion().getAnswers().stream().filter(Answer::isCorrect).toList();
            for (Answer questionAnswer : studentAnswer.getQuestion().getAnswers()) {
                if (!questionAnswer.isCorrect() && studentAnswers.contains(questionAnswer)) {
                    notCorrectStudentAnswers++;
                } else if (questionAnswer.isCorrect() && !studentAnswers.contains(questionAnswer)) {
                    notCorrectStudentAnswers++;
                }
            }
            int amountOfCorrectAnswers = correctAnswers.size();

            if (amountOfCorrectAnswers == 0 && studentAnswers.size() == 0) {
                return questionPoints * 1.0;
            } else if (notCorrectStudentAnswers == 0) {
                return questionPoints * 1.0;
            } else {
                double ratio = (studentAnswer.getQuestion().getAnswers().size() - notCorrectStudentAnswers) * 1.0 / studentAnswer.getQuestion().getAnswers().size() * 1.0;
                return questionPoints * 1.0 * ratio;
            }
        }
        return 0.0;
    }
}
