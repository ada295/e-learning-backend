package com.elearning.app.course;

import com.elearning.app.announcement.Announcement;
import com.elearning.app.announcement.AnnouncementRepository;
import com.elearning.app.lesson.Grade;
import com.elearning.app.lesson.GradeRepository;
import com.elearning.app.lesson.Lesson;
import com.elearning.app.responses.GradebookResponse;
import com.elearning.app.responses.coursedetails.*;
import com.elearning.app.user.UserAccount;
import com.elearning.app.user.UserRepository;
import com.elearning.app.user.UserRole;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GradeRepository gradeRepository;

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
        if (loggedUser.getRoles().contains(UserRole.TEACHER)) {
            return loggedUser.getCourses();
        }

        return courseRepository.findAll().stream().filter(c -> c.getStudents().contains(loggedUser)).collect(Collectors.toList());
    }

    @PostMapping("/courses/{id}/add-grade/{studentId}")
    public void addGrade(@PathVariable Long id, @RequestBody Grade grade, @PathVariable Long studentId) {
        UserAccount loggedUser = getUserAccount();
        if (!loggedUser.getRoles().contains(UserRole.TEACHER)) {
            return;
        }
        Course course = courseRepository.findById(id).get();
        UserAccount userAccount = userRepository.findById(studentId).get();
        grade.getCourses().add(course);
        grade.setStudent(userAccount);
        grade = gradeRepository.save(grade);
        course.getGrades().add(grade);
    }

    @PostMapping("/courses/grades/edit-grade")
    public void editGrade(@RequestBody Grade grade) {
        UserAccount loggedUser = getUserAccount();
        if (!loggedUser.getRoles().contains(UserRole.TEACHER)) {
            return;
        }
        Grade savedGrade = gradeRepository.findById(grade.getId()).get();
        savedGrade.setComment(grade.getComment());
        savedGrade.setValue(grade.getValue());
        savedGrade = gradeRepository.save(savedGrade);
    }

    @DeleteMapping("/courses/grades/delete-grade/{id}")
    public void deleteGrade(@PathVariable Long id) {
        UserAccount loggedUser = getUserAccount();
        if (!loggedUser.getRoles().contains(UserRole.TEACHER)) {
            return;
        }
        Grade grade = gradeRepository.findById(id).get();
        Set<Course> courses = grade.getCourses();
        for (Course cours : courses) {
            cours.getGrades().remove(grade);
        }
        grade.getCourses().clear();

        gradeRepository.delete(grade);
    }

    @GetMapping("/courses/{id}/grades")
    public List<GradebookResponse> getCoursesGrades(@PathVariable Long id) {
        UserAccount loggedUser = getUserAccount();
        Course course = courseRepository.findById(id).get();
        if (loggedUser.getRoles().contains(UserRole.TEACHER)) {
            List<GradebookResponse> responses = new ArrayList<>();
            Set<UserAccount> students = course.getStudents();
            for (UserAccount student : students) {
                responses.add(getGradebookResponse(student, course));
            }
            return responses;
        } else {
            return Collections.singletonList(getGradebookResponse(loggedUser, course));
        }
    }

    private GradebookResponse getGradebookResponse(UserAccount user, Course course) {
        List<Grade> grades = course.getGrades().stream().filter(e -> e.getStudent().equals(user))
                .collect(Collectors.toList());
        GradebookResponse gradebookResponse = new GradebookResponse();
        gradebookResponse.setCourse(getCourseDetailsCourseResponse(course));
        gradebookResponse.setStudent(getCourseDetailsStudentResponse(user));
        gradebookResponse.setAvg(calcAvg(grades));
        gradebookResponse.setGrades(grades);
        return gradebookResponse;
    }

    private Double calcAvg(List<Grade> grades) {
        Double res = 0.0;
        int c = 0;
        for (Grade grade : grades) {
            if (grade.getValue() != null) {
                res += grade.getValue();
                c++;
            }
        }
        if (c > 0) {
            return res / c;
        }
        return 0.0;
    }


    @GetMapping("/courses/{id}")
    public CourseDetailsResponse getCourse(@PathVariable Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isPresent()) {
            UserAccount loggedUser = getUserAccount();
            Course course = optionalCourse.get();
            if (!loggedUser.getRoles().contains(UserRole.TEACHER)) {
                if (!course.getStudents().contains(loggedUser)) {
                    throw new RuntimeException("Not access to course");
                }
            }
            //odpowiedz zawierajaca wszystkie wymagane przez course details dane
            CourseDetailsResponse response = new CourseDetailsResponse();

            CourseDetailsCourseResponse courseResponse = getCourseDetailsCourseResponse(course);

            //lista lekcji
            List<Lesson> lessons = course.getLessons();
            List<CourseDetailsLessonResponse> lessonResponses = new ArrayList<>();

            for (int i = lessons.size() - 1; i >= 0; i--) {
                CourseDetailsLessonResponse lessonResponse = new CourseDetailsLessonResponse();
                lessonResponse.setId(lessons.get(i).getId());
                lessonResponse.setName(lessons.get(i).getName());
                lessonResponses.add(lessonResponse);
            }

            List<Announcement> announcements = course.getAnnouncements();
            List<CourseDetailsAnnouncementResponse> announcementsResponse = new ArrayList<>();
            for (int i = announcements.size() - 1; i >= 0; i--) {
                CourseDetailsAnnouncementResponse announcementResponse = new CourseDetailsAnnouncementResponse();
                announcementResponse.setId(announcements.get(i).getId());
                announcementResponse.setName(announcements.get(i).getName());
                announcementResponse.setDate(announcements.get(i).getDate());
                announcementResponse.setDescription(announcements.get(i).getDescription());
                announcementsResponse.add(announcementResponse);
            }


//            lista studentow
            Set<UserAccount> students = course.getStudents();
            List<CourseDetailsStudentResponse> studentResponse = new ArrayList<>();
            for (UserAccount student : students) {
                CourseDetailsStudentResponse studentResp = getCourseDetailsStudentResponse(student);
                studentResponse.add(studentResp);
            }

            response.setCourse(courseResponse);
            response.setAnnouncements(announcementsResponse);
            response.setLessons(lessonResponses);
            response.setStudents(studentResponse);

            return response;
        }

        return null;
    }

    private static CourseDetailsStudentResponse getCourseDetailsStudentResponse(UserAccount student) {
        CourseDetailsStudentResponse studentResp = new CourseDetailsStudentResponse();
        studentResp.setId(student.getId());
        studentResp.setFirstName(student.getFirstName());
        studentResp.setLastName(student.getLastName());
        studentResp.setEmail(student.getEmail());
        return studentResp;
    }

    private static CourseDetailsCourseResponse getCourseDetailsCourseResponse(Course course) {
        CourseDetailsCourseResponse courseResponse = new CourseDetailsCourseResponse();
        courseResponse.setId(course.getId());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setName(course.getName());
        courseResponse.setAccessCode(course.getAccessCode());
        courseResponse.setFinished(course.isFinished());
        return courseResponse;
    }

    @PostMapping("/courses")
    public ResponseEntity addCourse(@RequestBody Course course) {
        UserAccount loggedUser = getUserAccount();
        if (!loggedUser.getRoles().contains(UserRole.TEACHER)) {
            throw new RuntimeException("Not teacher");
        }
        UserAccount teacher = getUserAccount();

        String name = course.getName();

        Optional<Course> optionalCourse = teacher.getCourses().stream()
                .filter(e -> e.getName().equals(name))
                .findFirst();

        if (optionalCourse.isPresent()) {
            return new ResponseEntity("Kurs z podaną nazwą już istnieje!", HttpStatus.BAD_REQUEST);
        }

        course.setId(null);
        course.setAccessCode(generateCourseCode());
        course.setOwner(teacher);
        course = courseRepository.save(course);
        teacher.getCourses().add(course);

        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping(path = "/courses/{courseId}/announcements")
    public ResponseEntity<Announcement> saveAnnouncements(@PathVariable Long courseId, @RequestBody Announcement announcement)
            throws IOException {
        UserAccount loggedUser = getUserAccount();
        if (!loggedUser.getRoles().contains(UserRole.TEACHER)) {
            throw new RuntimeException("Not teacher");
        }
        Course course = courseRepository.findById(courseId).get();
        announcement.setId(null);
        announcement.setDate(LocalDate.now(ZoneId.systemDefault()));
        announcement.setCourse(course);
        announcement = announcementRepository.save(announcement);
        course.getAnnouncements().add(announcement);

        return new ResponseEntity<>(announcement, HttpStatus.OK);
    }

    @DeleteMapping(path = "/courses/{courseId}/announcements/{announcementId}")
    public ResponseEntity deleteAnnouncement(@PathVariable Long courseId, @PathVariable Long announcementId)
            throws IOException {
        UserAccount loggedUser = getUserAccount();
        if (!loggedUser.getRoles().contains(UserRole.TEACHER)) {
            throw new RuntimeException("Not teacher");
        }
        Course course = courseRepository.findById(courseId).get();
        Announcement announcement = announcementRepository.findById(announcementId).get();
        course.getAnnouncements().remove(announcement);
        announcementRepository.delete(announcement);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/courses/{id}/regenerate-access-code")
    public ResponseEntity regenerateAccessCode(@PathVariable Long id) {
        UserAccount loggedUser = getUserAccount();
        if (!loggedUser.getRoles().contains(UserRole.TEACHER)) {
            throw new RuntimeException("Not teacher");
        }
        Course course = courseRepository.findById(id).get();
        course.setAccessCode(generateCourseCode());
        courseRepository.save(course);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/courses/{id}/remove-student/{studentId}")
    public ResponseEntity removeStudentFromCourse(@PathVariable Long id, @PathVariable Long studentId) {
        UserAccount loggedUser = getUserAccount();
        if (!loggedUser.getRoles().contains(UserRole.TEACHER)) {
            throw new RuntimeException("Not teacher");
        }
        UserAccount userAccount = userRepository.findById(studentId).get();
        Course course = courseRepository.findById(id).get();
        userAccount.getCoursesAsStudent().remove(course);
        course.getStudents().remove(userAccount);
        courseRepository.save(course);
        userRepository.save(userAccount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/courses/{code}/join")
    public ResponseEntity joinToCourse(@PathVariable String code) {
        UserAccount loggedUser = getUserAccount();
        if (!loggedUser.getRoles().contains(UserRole.STUDENT)) {
            throw new RuntimeException("Not student");
        }
        UserAccount userAccount = getUserAccount();
        Course course = courseRepository.findByAccessCode(code).get();
        Set<UserAccount> students = course.getStudents();
        students.add(userAccount);
        userAccount.getCoursesAsStudent().add(course);

        courseRepository.save(course);
        userRepository.save(userAccount);

        CourseDetailsCourseResponse response = new CourseDetailsCourseResponse();
        response.setId(course.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String generateCourseCode() {
        String code = "";
        do {
            code = RandomString.make(10);
        } while (courseRepository.findByAccessCode(code).isPresent());

        return code;
    }
}
