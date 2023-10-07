package com.elearning.app.course;

import com.elearning.app.announcement.Announcement;
import com.elearning.app.announcement.AnnouncementRepository;
import com.elearning.app.lesson.Lesson;
import com.elearning.app.responses.coursedetails.CourseDetailsAnnouncementResponse;
import com.elearning.app.responses.coursedetails.CourseDetailsCourseResponse;
import com.elearning.app.responses.coursedetails.CourseDetailsLessonResponse;
import com.elearning.app.responses.coursedetails.CourseDetailsResponse;
import com.elearning.app.user.UserAccount;
import com.elearning.app.user.UserRepository;
import com.elearning.app.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private UserRepository userRepository;

    private UserAccount getUserAccount() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        return userAccount;
    }

    @GetMapping("/courses")
    public List<Course> getCourses() {
        UserAccount loggedUser = getUserAccount();
        if(loggedUser.getRoles().contains(UserRole.TEACHER)) {
            return loggedUser.getCourses();
        }

        return courseRepository.findAll();
    }

    @GetMapping("/courses/{id}")
    public CourseDetailsResponse getCourse(@PathVariable Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isPresent()) {
            //odpowiedz zawierajaca wszystkie wymagane przez course details dane
            CourseDetailsResponse response = new CourseDetailsResponse();

            CourseDetailsCourseResponse courseResponse = new CourseDetailsCourseResponse();
            courseResponse.setId(optionalCourse.get().getId());
            courseResponse.setDescription(optionalCourse.get().getDescription());
            courseResponse.setName(optionalCourse.get().getName());
            courseResponse.setFinished(optionalCourse.get().isFinished());

            //lista lekcji
            List<Lesson> lessons = optionalCourse.get().getLessons();
            List<CourseDetailsLessonResponse> lessonResponses = new ArrayList<>();

            for (int i = lessons.size() - 1; i >= 0; i--) {
                CourseDetailsLessonResponse lessonResponse = new CourseDetailsLessonResponse();
                lessonResponse.setId(lessons.get(i).getId());
                lessonResponse.setName(lessons.get(i).getName());
                lessonResponses.add(lessonResponse);
            }

            List<Announcement> announcements = optionalCourse.get().getAnnouncements();
            List<CourseDetailsAnnouncementResponse> announcementsResponse = new ArrayList<>();
            for (int i = announcements.size() - 1; i >= 0; i--) {
                CourseDetailsAnnouncementResponse announcementResponse = new CourseDetailsAnnouncementResponse();
                announcementResponse.setId(announcements.get(i).getId());
                announcementResponse.setName(announcements.get(i).getName());
                announcementResponse.setDate(announcements.get(i).getDate());
                announcementResponse.setDescription(announcements.get(i).getDescription());
                announcementsResponse.add(announcementResponse);
            }


            //lista studentow
//            List<Student> students = optionalCourse.get().get();
//            List<Student> lessonResponses = new ArrayList<>();
//            for (Student lesson : students) {
//                StudentResponse lessonResponse = new StudentResponse();
//                lessonResponse.setId(lesson.getId());
//                lessonResponse.setName(lesson.getName());
//                lessonResponses.add(lessonResponse);
//            }

            response.setCourse(courseResponse);
            response.setAnnouncements(announcementsResponse);
            response.setLessons(lessonResponses);

            return response;
        }

        return null;
    }

    @PostMapping("/courses")
    public ResponseEntity addCourse(@RequestBody Course course) {
        //kto dodal ten kurs bedzie okreslone po zrobieniu logowania
        UserAccount teacher = userRepository.findById(1L).get();

        String name = course.getName();

        Optional<Course> optionalCourse = teacher.getCourses().stream()
                .filter(e -> e.getName().equals(name))
                .findFirst();

        if (optionalCourse.isPresent()) {
            return new ResponseEntity("Kurs z podaną nazwą już istnieje!", HttpStatus.BAD_REQUEST);
        }

        course.setId(null);
        course.setOwner(teacher);
        course = courseRepository.save(course);
        teacher.getCourses().add(course);

        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping(path = "/courses/{courseId}/announcements")
    public ResponseEntity<Announcement> saveAnnouncements(@PathVariable Long courseId, @RequestBody Announcement announcement)
            throws IOException {
        Course course = courseRepository.findById(courseId).get();
        announcement.setId(null);
        announcement.setDate(LocalDate.now(ZoneId.systemDefault()));
        announcement.setCourse(course);
        announcement = announcementRepository.save(announcement);
        course.getAnnouncements().add(announcement);

        return new ResponseEntity<>(announcement, HttpStatus.OK);
    }
}
