package com.elearning.app.course;

import com.elearning.app.exam.Exam;
import com.elearning.app.lesson.Lesson;
import com.elearning.app.responses.coursedetails.CourseDetailsCourseResponse;
import com.elearning.app.responses.coursedetails.CourseDetailsExamResponse;
import com.elearning.app.responses.coursedetails.CourseDetailsLessonResponse;
import com.elearning.app.responses.coursedetails.CourseDetailsResponse;
import com.elearning.app.teacher.Teacher;
import com.elearning.app.teacher.TeacherRepository;
import org.apache.tomcat.util.http.parser.Authorization;
import org.apache.tomcat.websocket.AuthenticatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @GetMapping("/courses")
    public List<Course> getCourses() {
        // TODO: 05/02/2023 nalezy zmienic zwracane wartosci, chcemy zwrocic tylko nazwy kursow 
        return courseRepository.findAll();
    }

    @GetMapping("/courses/{id}")
    public CourseDetailsResponse getCourse(@PathVariable Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isPresent()) {
            //odpowiedz zawierajaca wszystkie wymagane przez course details dane
            CourseDetailsResponse response = new CourseDetailsResponse();

            CourseDetailsCourseResponse courseResponse = new CourseDetailsCourseResponse();
            courseResponse.setId(optionalCourse.get().getId());
            courseResponse.setDescription(optionalCourse.get().getDescription());
            courseResponse.setName(optionalCourse.get().getName());
            courseResponse.setFinished(optionalCourse.get().isFinished());

            //lista lekcji
            List<Lesson> lessons = optionalCourse.get().getLessons();
            List<CourseDetailsLessonResponse> lessonResponses = new ArrayList<>();

            for (int i = lessons.size() - 1; i >= 0; i--) {
                CourseDetailsLessonResponse lessonResponse = new CourseDetailsLessonResponse();
                lessonResponse.setId(lessons.get(i).getId());
                lessonResponse.setName(lessons.get(i).getName());
                lessonResponses.add(lessonResponse);
            }

            //lista exams
            List<Exam> exams = optionalCourse.get().getExams();
            List<CourseDetailsExamResponse> examResponses = new ArrayList<>();
            for (Exam exam : exams) {
                CourseDetailsExamResponse examResponse = new CourseDetailsExamResponse();
                examResponse.setId(exam.getId());
                examResponse.setName(exam.getName());
                examResponses.add(examResponse);
            }

            //lista studentow
//            List<Student> students = optionalCourse.get().get();
//            List<Student> lessonResponses = new ArrayList<>();
//            for (Student lesson : students) {
//                StudentResponse lessonResponse = new StudentResponse();
//                lessonResponse.setId(lesson.getId());
//                lessonResponse.setName(lesson.getName());
//                lessonResponses.add(lessonResponse);
//            }

            response.setCourse(courseResponse);
            response.setLessons(lessonResponses);
            response.setExams(examResponses);

            return response;
        }

        return null;
    }

    @PostMapping("/courses")
    public ResponseEntity addCourse(@RequestBody Course course) {
        //kto dodal ten kurs bedzie okreslone po zrobieniu logowania
        Teacher teacher = teacherRepository.findById(1L).get();

        String name = course.getName();

        Optional<Course> optionalCourse = teacher.getCourses().stream()
                .filter(e-> e.getName().equals(name))
                .findFirst();

        if(optionalCourse.isPresent()) {
            return new ResponseEntity("Kurs z podaną nazwą już istnieje!", HttpStatus.BAD_REQUEST);
        }

        course.setId(null);
        course.setTeacher(teacher);
        course = courseRepository.save(course);
        teacher.getCourses().add(course);

        return new ResponseEntity(HttpStatus.OK);
    }
}
