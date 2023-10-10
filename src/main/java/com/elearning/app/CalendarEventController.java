package com.elearning.app;

import com.elearning.app.user.UserAccount;
import com.elearning.app.user.UserRepository;
import com.elearning.app.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CalendarEventController {
    @Autowired
    private CalendarEventRepository calendarEventRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/calendar-events")
    public List<CalendarEvent> getAll(){
        return calendarEventRepository.findAll();
    }

    @PostMapping("/calendar-events")
    public void addEvent(@RequestBody CalendarEvent event) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();
        if (!userAccount.getRoles().contains(UserRole.TEACHER)) {
            return;
        }
        event.setId(null);
        calendarEventRepository.save(event);
    }

    @DeleteMapping("/calendar-events/{id}")
    public void deleteEvent(@PathVariable Long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();
        if (!userAccount.getRoles().contains(UserRole.TEACHER)) {
            return;
        }
        calendarEventRepository.deleteById(id);
    }
}
