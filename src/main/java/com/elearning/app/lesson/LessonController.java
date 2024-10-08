package com.elearning.app.lesson;


import com.elearning.app.course.Course;
import com.elearning.app.course.CourseRepository;
import com.elearning.app.responses.coursedetails.CourseDetailsStudentResponse;
import com.elearning.app.user.UserAccount;
import com.elearning.app.user.UserRepository;
import com.elearning.app.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LessonController {

    @Autowired
    private LessonRepository repository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStudentRepository taskStudentRepository;

    @GetMapping("/lessons")
    public List<Lesson> getLessonsByCourseId(@RequestParam Long courseId){
        return repository.findAllByCourseId(courseId);
    }

    @GetMapping("/lessons/{lessonId}/materials")
    public List<Material> getMaterials(@PathVariable Long lessonId) {
        return materialRepository.findAllByLessonId(lessonId);
    }

    @GetMapping("/lessons/{lessonId}")
    public Lesson getLesson(@PathVariable Long lessonId) {
        return repository.findById(lessonId).get();
    }

    @GetMapping(path = "/materials/{id}/download")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable Long id) throws IOException {
        Material material = materialRepository.findById(id).get();
        String filename = material.getFilename();

        File file = new File("C:\\Users\\user\\Desktop\\Dokumenty\\materials\\" + id + "\\" + filename);
        Path path = file.toPath();
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @GetMapping(path = "/lessons/{lessonId}/tasks")
    public List<Task> getTasks(@PathVariable Long lessonId) {
        return taskRepository.findAllByLessonId(lessonId);
    }

    @GetMapping(path = "/lessons/{lessonId}/student-tasks")
    public List<TaskToDo> getTasksToDo(@PathVariable Long lessonId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();

        List<TaskToDo> tasksToDo = new ArrayList<>();
        List<Task> allByLessonId = taskRepository.findAllByLessonId(lessonId);
        for (Task task : allByLessonId) {
            TaskToDo taskToDo = buildTaskToDo(task, studentId);
            tasksToDo.add(taskToDo);
        }

        return tasksToDo;
    }

    @GetMapping(path = "/tasks/{taskId}/student-tasks-solutions")
    public List<StudentTaskSolution> getTaskSolutions(@PathVariable Long taskId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();
        if (!userAccount.getRoles().contains(UserRole.TEACHER)) {
            return new ArrayList<>();
        }

        List<StudentTaskSolution> result = new ArrayList<>();
        Task task = taskRepository.findById(taskId).get();
        Set<UserAccount> students = task.getLesson().getCourse().getStudents();
        for (UserAccount student : students) {
            TaskToDo taskToDo = buildTaskToDo(task, student.getId());
            CourseDetailsStudentResponse studentResponse = new CourseDetailsStudentResponse();
            studentResponse.setId(student.getId());
            studentResponse.setFirstName(student.getFirstName());
            studentResponse.setEmail(student.getEmail());
            studentResponse.setLastName(student.getLastName());
            StudentTaskSolution studentTaskSolution = new StudentTaskSolution();
            studentTaskSolution.setTask(taskToDo);
            studentTaskSolution.setStudent(studentResponse);
            result.add(studentTaskSolution);
        }

        return result;
    }

    @GetMapping(path = "/student-tasks/{taskId}")
    public TaskToDo getTaskToDo(@PathVariable Long taskId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();

        Task task = taskRepository.findById(taskId).get();

        return buildTaskToDo(task, studentId);
    }

    private TaskToDo buildTaskToDo(Task task, Long studentId) {
        TaskToDo taskToDo = new TaskToDo();
        taskToDo.setTask(task);

        Optional<TaskStudent> taskStudent = task.getTaskStudents().stream()
                .filter(e -> e.getOwner() != null && studentId.equals(e.getOwner().getId()))
                .findFirst();

        UserAccount userAccount = userRepository.findById(studentId).get();
        taskToDo.setStudent(userAccount);

        if (taskStudent.isPresent()) {
            taskToDo.setTaskStudent(taskStudent.get());
            TaskStudentStatus status = taskStudent.get().getStatus();
            taskToDo.setStatus(status);
            if (status == TaskStudentStatus.OCENIONE) {
                taskToDo.setIcon("star");
                taskToDo.setGrade(taskStudent.get().getGrade().getValue());
            } else if (status == TaskStudentStatus.WYKONANE) {
                taskToDo.setIcon("done");
            }
        } else if (task.getEndDate().after(new Date())) {
            taskToDo.setStatus(TaskStudentStatus.AKTYWNE);
            taskToDo.setIcon("alarm");
        } else {
            taskToDo.setStatus(TaskStudentStatus.NIEWYKONANE);
            taskToDo.setIcon("close");
        }
        return taskToDo;
    }

    @PostMapping(path = "/lessons/{lessonId}/materials")
    public ResponseEntity<Material> saveMaterial(@PathVariable Long lessonId, @RequestBody Material material)
            throws IOException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();
        if (!userAccount.getRoles().contains(UserRole.TEACHER)) {
            return ResponseEntity.badRequest().body(null);
        }
        Lesson lesson = repository.findById(lessonId).get();

        Course course = lesson.getCourse();
        if (course.isFinished()) {
            throw new RuntimeException("Kurs Zakończony!");
        }
        material.setId(null);
        material.setLesson(lesson);
        material = materialRepository.save(material);

        return new ResponseEntity<>(material, HttpStatus.OK);
    }

    @PostMapping(path = "/lessons/{lessonId}/tasks")
    public ResponseEntity<Task> saveTask(@PathVariable Long lessonId, @RequestBody Task task)
            throws IOException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();
        if (!userAccount.getRoles().contains(UserRole.TEACHER)) {
            return ResponseEntity.badRequest().body(null);
        }
        Lesson lesson = repository.findById(lessonId).get();
        Course course = lesson.getCourse();
        if (course.isFinished()) {
            throw new RuntimeException("Kurs Zakończony!");
        }
        task.setId(null);
        task.setLesson(lesson);
        task = taskRepository.save(task);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PostMapping(path = "tasks/{id}/edit")
    public ResponseEntity<Task> editTask(@PathVariable Long id, @RequestBody Task task)
            throws IOException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();
        if (!userAccount.getRoles().contains(UserRole.TEACHER)) {
            return ResponseEntity.badRequest().body(null);
        }
        Task origTask = taskRepository.findById(id).get();
        Course course = origTask.getLesson().getCourse();
        if (course.isFinished()) {
            throw new RuntimeException("Kurs Zakończony!");
        }
        origTask.setEndDate(task.getEndDate());
        origTask.setDescription(task.getDescription());
        origTask = taskRepository.save(origTask);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PostMapping(path = "/courses/{courseId}/lessons")
    public ResponseEntity<Lesson> saveLesson(@PathVariable Long courseId, @RequestBody Lesson lesson)
            throws IOException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();
        if (!userAccount.getRoles().contains(UserRole.TEACHER)) {
            return ResponseEntity.badRequest().body(null);
        }

        Course course = courseRepository.findById(courseId).get();
        if (course.isFinished()) {
            throw new RuntimeException("Kurs Zakończony!");
        }
        lesson.setId(null);
        lesson.setCourse(course);
        lesson = repository.save(lesson);
        course.getLessons().add(lesson);

        return new ResponseEntity<>(lesson, HttpStatus.OK);
    }

    @PostMapping(path = "/materials/{materialId}/upload")
    public ResponseEntity uploadFile(@RequestParam(required = false) MultipartFile file, @PathVariable Long materialId)
            throws IOException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();
        if (!userAccount.getRoles().contains(UserRole.TEACHER)) {
            return ResponseEntity.badRequest().body(null);
        }

        Material material = materialRepository.findById(materialId).get();
        if (material.getLesson().getCourse().isFinished()) {
            throw new RuntimeException("Kurs Zakończony!");
        }
        String filename = material.getFilename();

        try {
            File fileTmp = new File("C:\\Users\\user\\Desktop\\Dokumenty\\materials\\" + material.getId() + "\\" + filename);
            File directory = new File("C:\\Users\\user\\Desktop\\Dokumenty\\materials\\" + material.getId());
            directory.mkdirs();
            Files.copy(file.getInputStream(), fileTmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/tasks/{taskId}/upload-solution")
    public ResponseEntity uploadTaskSolution(@RequestParam(required = false) MultipartFile file,
                                             @PathVariable Long taskId) throws IOException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();
        if (!userAccount.getRoles().contains(UserRole.STUDENT)) {
            return ResponseEntity.badRequest().body(null);
        }

        Task task = taskRepository.findById(taskId).get();
        if (task.getLesson().getCourse().isFinished()) {
            throw new RuntimeException("Kurs Zakończony!");
        }
        String filename = file.getOriginalFilename();
        TaskStudent taskStudent = new TaskStudent();
        taskStudent.setFilename(filename);
        taskStudent.setStatus(TaskStudentStatus.WYKONANE);
        taskStudent.setTask(task);
        taskStudent.setOwner(userAccount);

        taskStudentRepository.save(taskStudent);

        try {
            File fileTmp = new File("C:\\Users\\user\\Desktop\\Dokumenty\\tasks\\" + task.getId() + "\\students\\" + studentId + "\\" + filename);
            File directory = new File("C:\\Users\\user\\Desktop\\Dokumenty\\tasks\\" + task.getId() + "\\students\\" + studentId);
            directory.mkdirs();
            Files.copy(file.getInputStream(), fileTmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }


    @PostMapping(path = "/tasks/{taskId}/upload")
    public ResponseEntity uploadTaskSolutionFile(@RequestParam(required = false) MultipartFile file, @PathVariable Long taskId)
            throws IOException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();
        UserAccount student = userRepository.findById(studentId).get();

        Task task = taskRepository.findById(taskId).get();
        if (task.getLesson().getCourse().isFinished()) {
            throw new RuntimeException("Kurs Zakończony!");
        }
        String filename = file.getOriginalFilename();

        try {
            File fileTmp = new File("C:\\Users\\user\\Desktop\\Dokumenty\\task_solutions\\" + task.getId() + "\\" + studentId + "\\" + filename);
            File directory = new File("C:\\Users\\user\\Desktop\\Dokumenty\\task_solutions\\" + task.getId() + "\\" + studentId);
            directory.mkdirs();
            Files.copy(file.getInputStream(), fileTmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        TaskStudent taskStudent = new TaskStudent();
        taskStudent.setOwner(student);
        taskStudent.setStatus(TaskStudentStatus.WYKONANE);
        taskStudent.setTask(task);
        taskStudent.setFilename(filename);

        taskStudentRepository.save(taskStudent);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/tasks-solution/{id}/download")
    public ResponseEntity<Resource> downloadTaskSolution(@PathVariable Long id) throws IOException {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();
        UserAccount student = userRepository.findById(studentId).get();

        TaskStudent taskStudent = taskStudentRepository.findById(id).get();
        String filename = taskStudent.getFilename();

        File file = new File("C:\\Users\\user\\Desktop\\Dokumenty\\task_solutions\\" + taskStudent.getTask().getId() + "\\" + studentId + "\\" + filename);
        Path path = file.toPath();
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }


    @DeleteMapping(path = "/tasks/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        if (!userAccount.getRoles().contains(UserRole.TEACHER)) {
            return;
        }
        Task task = taskRepository.findById(taskId).get();
        if (task.getLesson().getCourse().isFinished()) {
            throw new RuntimeException("Kurs Zakończony!");
        }

        List<TaskStudent> taskStudents = task.getTaskStudents();
        for (TaskStudent taskStudent : taskStudents) {
            if (taskStudent.getGrade() != null) {
                Grade grade = taskStudent.getGrade();
                grade.setTaskStudent(null);
                grade.getLesson().getGrades().remove(grade);
                for (Course cours : grade.getCourses()) {
                    cours.getGrades().remove(grade);
                }
                gradeRepository.delete(grade);
            }

            taskStudentRepository.delete(taskStudent);
        }
        taskRepository.delete(task);
    }

    @DeleteMapping(path = "/materials/{materialId}/delete")
    public void deleteMaterial(@PathVariable Long materialId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();
        if (!userAccount.getRoles().contains(UserRole.TEACHER)) {
            return;
        }
        Optional<Material> byId = materialRepository.findById(materialId);
        if (byId.get().getLesson().getCourse().isFinished()) {
            throw new RuntimeException("Kurs Zakończony!");
        }
        materialRepository.deleteById(materialId);
    }

    @DeleteMapping(path = "/student-tasks/{id}/delete")
    public void deleteTaskSolution(@PathVariable Long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userRepository.findByEmail(principal.getUsername()).get();
        Long studentId = userAccount.getId();
        if (!userAccount.getRoles().contains(UserRole.STUDENT)) {
            return;
        }

        TaskStudent taskStudent = taskStudentRepository.findById(id).get();
        Course course = taskStudent.getTask().getLesson().getCourse();
        if (course.isFinished()) {
            throw new RuntimeException("Kurs Zakończony!");
        }
        if (taskStudent.getOwner().equals(userAccount)) {
            taskStudentRepository.delete(taskStudent);
            File file = new File("C:\\Users\\user\\Desktop\\Dokumenty\\task_solutions\\" + taskStudent.getTask().getId() + "\\" + studentId + "\\" + taskStudent.getFilename());
            file.delete();
        }
    }
}
