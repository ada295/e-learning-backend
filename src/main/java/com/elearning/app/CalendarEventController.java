package com.elearning.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CalendarEventController {
    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @GetMapping("/calendar-events")
    public List<CalendarEvent> getAll(){
        return calendarEventRepository.findAll();
    }

    @PostMapping("/calendar-events")
    public void addEvent(@RequestBody CalendarEvent event) {
        event.setId(null);
        calendarEventRepository.save(event);
    }
}
