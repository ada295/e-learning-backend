package com.elearning.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {

    @Autowired
    private UserRepository repository;


    @PostMapping("/change-data-as-admin")
    public ResponseEntity<?> editDataAsAdmin(@RequestBody UserAccountRequest user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(e -> e.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.status(403).build();
        }

        UserAccount userAccount = repository.findById(user.getId()).get();
        userAccount.setEmail(user.getEmail());
        userAccount.setFirstName(user.getFirstName());
        userAccount.setLastName(user.getLastName());

        if (user.getPassword() != null && !user.getPassword().equals("")) {
            userAccount.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        Map<String, Object> responseMap = new HashMap<>();

        repository.save(userAccount);
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/edit-profile")
    public ResponseEntity<?> editProfile(@RequestBody UserAccountRequest user) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = repository.findByEmail(principal.getUsername()).get();

        userAccount.setEmail(user.getEmail());
        userAccount.setFirstName(user.getFirstName());
        userAccount.setLastName(user.getLastName());

        if (user.getPassword() != null && !user.getPassword().equals("")) {
            userAccount.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        Map<String, Object> responseMap = new HashMap<>();

        repository.save(userAccount);
        return ResponseEntity.ok(responseMap);
    }

    @GetMapping("/students")
    public List<UserAccount> getStudents() {
        List<UserAccount> allByRoles = repository.findAllByRoles(Collections.singletonList(UserRole.STUDENT));
        allByRoles.sort(Comparator.comparing(UserAccount::getLastName));

        return allByRoles;
    }

    @GetMapping("/students/{id}")
    public UserAccount getStudentById(@PathVariable Long id) {
        return repository.findById(id).get();
    }

    @GetMapping("/my-profile")
    public UserAccount myProfile() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = repository.findByEmail(principal.getUsername()).get();

        return userAccount;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserAccount> getAccountById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(e -> e.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(repository.findById(id).get());
    }

    @GetMapping("/admins")
    public List<UserAccount> getAdmins() {
        List<UserAccount> allByRoles = repository.findAllByRoles(Collections.singletonList(UserRole.ADMIN));
        allByRoles.sort(Comparator.comparing(UserAccount::getLastName));
        return allByRoles;
    }

    @GetMapping("/teachers")
    public List<UserAccount> getTeachers() {
        List<UserAccount> allByRoles = repository.findAllByRoles(Collections.singletonList(UserRole.TEACHER));
        allByRoles.sort(Comparator.comparing(UserAccount::getLastName));
        return allByRoles;
    }

    @GetMapping("/users")
    public List<UserAccount> getAll() {
        List<UserAccount> users = getStudents();
        users.addAll(getAdmins());
        users.addAll(getTeachers());
        users.sort(Comparator.comparing(UserAccount::getLastName));

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
