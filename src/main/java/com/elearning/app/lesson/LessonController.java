package com.elearning.app.lesson;


import com.elearning.app.user.Student;
import com.elearning.app.user.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LessonController {

    @Autowired
    private LessonRepository repository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TaskStudentRepository taskStudentRepository;

    @GetMapping("/lessons")
    public List<Lesson> getLessonsByCourseId(@RequestParam Long courseId) {
        return repository.findAllByCourseId(courseId);
    }

    @GetMapping("/lessons/{lessonId}/materials")
    public List<Material> getMaterials(@PathVariable Long lessonId) {
        return materialRepository.findAllByLessonId(lessonId);
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
        Long studentId = 1L; //jak bedzie logowanie to automatycznie

        List<TaskToDo> tasksToDo = new ArrayList<>();
        List<Task> allByLessonId = taskRepository.findAllByLessonId(lessonId);
        for (Task task : allByLessonId) {
            TaskToDo taskToDo = buildTaskToDo(task, studentId);
            tasksToDo.add(taskToDo);
        }

        return tasksToDo;
    }

    @GetMapping(path = "/student-tasks/{taskId}")
    public TaskToDo getTaskToDo(@PathVariable Long taskId) {
        Long studentId = 1L; //jak bedzie logowanie to automatycznie

        Task task = taskRepository.findById(taskId).get();

        return buildTaskToDo(task, studentId);
    }

    private TaskToDo buildTaskToDo(Task task, Long studentId) {
        TaskToDo taskToDo = new TaskToDo();
        taskToDo.setTask(task);

        Optional<TaskStudent> taskStudent = task.getTaskStudents().stream()
                .filter(e -> e.getStudent() != null && studentId.equals(e.getStudent().getId())).findFirst();

        if (taskStudent.isPresent()) {
            taskToDo.setTaskStudent(taskStudent.get());
            TaskStudentStatus status = taskStudent.get().getStatus();
            taskToDo.setStatus(status);
            if (status == TaskStudentStatus.OCENIONE) {
                taskToDo.setIcon("star");
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
        Lesson lesson = repository.findById(lessonId).get();
        material.setId(null);
        material.setLesson(lesson);
        material = materialRepository.save(material);

        return new ResponseEntity<>(material, HttpStatus.OK);
    }

    @PostMapping(path = "/lessons/{lessonId}/tasks")
    public ResponseEntity<Task> saveTask(@PathVariable Long lessonId, @RequestBody Task task)
            throws IOException {
        Lesson lesson = repository.findById(lessonId).get();
        task.setId(null);
        task.setLesson(lesson);
        task = taskRepository.save(task);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PostMapping(path = "/materials/{materialId}/upload")
    public ResponseEntity uploadFile(@RequestParam(required = false) MultipartFile file, @PathVariable Long materialId)
            throws IOException {
        Material material = materialRepository.findById(materialId).get();
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

    @PostMapping(path = "/tasks/{taskId}/upload")
    public ResponseEntity uploadTaskSolutionFile(@RequestParam(required = false) MultipartFile file, @PathVariable Long taskId)
            throws IOException {
        Long studentId = 1L;
        Student student = studentRepository.findById(studentId).get();

        Task task = taskRepository.findById(taskId).get();
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
        taskStudent.setStudent(student);
        taskStudent.setStatus(TaskStudentStatus.WYKONANE);
        taskStudent.setTask(task);
        taskStudent.setFilename(filename);

        taskStudentRepository.save(taskStudent);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/tasks-solution/{id}/download")
    public ResponseEntity<Resource> downloadTaskSolution(@PathVariable Long id) throws IOException {
        Long studentId = 1L;
        Student student = studentRepository.findById(studentId).get();

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


    @DeleteMapping(path = "/materials/{materialId}/delete")
    public void deleteMaterial(@PathVariable Long materialId) {
        materialRepository.deleteById(materialId);
    }

    @DeleteMapping(path = "/student-tasks/{id}/delete")
    public void deleteTaskSolution(@PathVariable Long id) {
        Long studentId = 1L;
        taskStudentRepository.deleteById(id);
    }
}
