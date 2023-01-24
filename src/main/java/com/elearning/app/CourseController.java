package com.elearning.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CourseController {

    @GetMapping("/courses")
    public List<Course> getCourses () {
        List<Course> courses = new ArrayList<>();

        courses.add(new Course(1L,"Matma", "Liczby rzeczywiste"));
        courses.add(new Course(2L, "Biologia", "Człowiek"));
        courses.add(new Course(3L, "IT", "Prolog"));
        courses.add(new Course(4L, "Chemia", "Pierwiastki chemiczne"));
        return courses;
    }

    @PostMapping("/courses")
    public void saveCourses (@RequestBody List<Course> courses) {
        System.out.println(courses);
    }
}
