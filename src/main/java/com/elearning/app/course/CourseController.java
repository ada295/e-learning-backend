package com.elearning.app.course;

import com.elearning.app.course.Course;
import com.elearning.app.course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Course getCourse (@PathVariable Long id) {
        Optional<Course> optionalCourse = repository.findById(id);
        return optionalCourse.orElse(null);
    }

    @PostMapping("/courses")
    public void saveCourses (@RequestBody List<Course> courses) {
        System.out.println(courses);
    }
}
