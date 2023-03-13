package com.elearning.app.teacher;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TeacherController {

    @Autowired
    private TeacherRepository repository;

    @PostMapping("/teachers")
    public void addTeacher (@RequestBody Teacher teacher) {
        teacher.setId(null);
        repository.save(teacher);
    }


}
