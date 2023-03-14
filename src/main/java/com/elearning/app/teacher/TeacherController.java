package com.elearning.app.teacher;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TeacherController {

    @Autowired
    private TeacherRepository repository;

    @GetMapping("/teachers")
    public List<Teacher> getTeachers(){
        return repository.findAll();
    }

    @PostMapping("/teachers")
    public void addTeacher (@RequestBody Teacher teacher) {
        teacher.setId(null);
        repository.save(teacher);
    }


}
