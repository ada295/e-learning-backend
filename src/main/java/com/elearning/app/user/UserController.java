package com.elearning.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/students")
    public List<UserAccount> getStudents() {
        return repository.findAllByRoles(Collections.singletonList(UserRole.STUDENT));
    }

    @GetMapping("/admins")
    public List<UserAccount> getAdmins() {
        return repository.findAllByRoles(Collections.singletonList(UserRole.ADMIN));
    }

    @GetMapping("/teachers")
    public List<UserAccount> getTeachers() {
        return repository.findAllByRoles(Collections.singletonList(UserRole.TEACHER));
    }

    @GetMapping("/users")
    public List<UserAccount> getAll() {
        List<UserAccount> users = getStudents();
        users.addAll(getAdmins());
        users.addAll(getTeachers());

        return users;
    }

    @GetMapping("/students/search")
    public List<UserAccount> searchUsers(@RequestParam String name, @RequestParam String surname) {
        if ((name == null || "".equals(name)) && (surname == null || "".equals(surname))) {
            //zwracamy wszystkich uzytkownikow
            return repository.findAll();
        } else if (name == null || "".equals(name)) {
            //szukamy po surname
            return repository.findByLastName(surname);
        } else if (surname == null || "".equals(surname)) {
            //szukamy po name
            return repository.findByFirstName(name);
        } else {
            //wyniki zgodne z name i surname
            return repository.findAllByFirstNameAndLastName(name, surname);
        }
    }
}
