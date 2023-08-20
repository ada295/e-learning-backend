package com.elearning.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {

    @Autowired
    private StudentRepository repository;

    @GetMapping("/users")
    public List<Student> getUsers() {
        return repository.findAll();
    }

    @GetMapping("/users/{id}")
    public Student getUserById(@PathVariable Long id) {
        Optional<Student> userTestOptional = repository.findById(id);
        if (userTestOptional.isPresent()) {
            return userTestOptional.get();
        }
        return null;
    }

    @GetMapping("/users/search")
    public List<Student> searchUsers(@RequestParam String name, @RequestParam String surname) {
        if ((name == null || "".equals(name)) && (surname == null || "".equals(surname))) {
            //zwracamy wszystkich uzytkownikow
            return repository.findAll();
        } else if (name == null || "".equals(name)) {
            //szukamy po surname
            return repository.findAllBySurname(surname);
        } else if (surname == null || "".equals(surname)) {
            //szukamy po name
            return repository.findAllByName(name);
        } else {
            //wyniki zgodne z name i surname
            return repository.findAllByNameAndSurname(name,surname);
        }
    }

    @PostMapping("/users")
    public void saveUsers(@RequestBody List<Student> userTests) {
        repository.saveAll(userTests);
    }

    @DeleteMapping("/users")
    public void deleteUsers(@RequestBody List<Student> userTests) {
        repository.deleteAll(userTests);
    }
}
