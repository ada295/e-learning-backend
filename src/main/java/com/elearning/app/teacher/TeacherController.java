package com.elearning.app.teacher;


import com.elearning.app.user.UserAccount;
import com.elearning.app.user.UserRepository;
import com.elearning.app.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TeacherController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/teachers")
    public List<UserAccount> getTeachers(){
        return repository.findAllByRoles(Collections.singletonList(UserRole.TEACHER));
    }
}
