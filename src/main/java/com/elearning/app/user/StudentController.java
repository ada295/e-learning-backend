package com.elearning.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class StudentController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/users")
    public List<UserAccount> getUsers() {
        return repository.findAllByRoles(Collections.singletonList(UserRole.STUDENT));
    }

    @GetMapping("/users/search")
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
