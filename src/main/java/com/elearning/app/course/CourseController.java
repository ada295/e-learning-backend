package com.elearning.app.course;

import com.elearning.app.Student;
import com.elearning.app.course.Course;
import com.elearning.app.course.CourseRepository;
import com.elearning.app.lesson.Lesson;
import com.elearning.app.responses.CourseDetailsResponse;
import com.elearning.app.responses.CourseResponse;
import com.elearning.app.responses.LessonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CourseController {

    @Autowired
    private CourseRepository repository;

    @GetMapping("/courses")
    public List<Course> getCourses () {
        return repository.findAll();
    }

    @GetMapping("/courses/{id}")
    public CourseDetailsResponse getCourse (@PathVariable Long id) {
        Optional<Course> optionalCourse = repository.findById(id);

        if(optionalCourse.isPresent()) {
            //odpowiedz zawierajaca wszystkie wymagane przez course details dane
            CourseDetailsResponse response = new CourseDetailsResponse();

            CourseResponse courseResponse = new CourseResponse();
            courseResponse.setId(optionalCourse.get().getId());
            courseResponse.setDescription(optionalCourse.get().getDescription());
            courseResponse.setName(optionalCourse.get().getName());
            courseResponse.setFinished(optionalCourse.get().isFinished());

            //lista lekcji
            List<Lesson> lessons = optionalCourse.get().getLessons();
            List<LessonResponse> lessonResponses = new ArrayList<>();
            for (Lesson lesson : lessons) {
                LessonResponse lessonResponse = new LessonResponse();
                lessonResponse.setId(lesson.getId());
                lessonResponse.setName(lesson.getName());
                lessonResponses.add(lessonResponse);
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

            return response;
        }

        return null;
    }

    @PostMapping("/courses")
    public void saveCourses (@RequestBody List<Course> courses) {
        System.out.println(courses);
    }
}
