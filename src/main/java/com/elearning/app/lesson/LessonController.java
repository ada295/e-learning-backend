package com.elearning.app.lesson;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LessonController {

    @Autowired
    private LessonRepository repository;

    @GetMapping("/lessons")
    public List<Lesson> getLessonsByCourseId (@RequestParam Long courseId) {
        return repository.findAllByCourseId(courseId);
    }
}
