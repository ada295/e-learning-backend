package com.elearning.app.lesson;


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
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LessonController {

    @Autowired
    private LessonRepository repository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private TaskRepository taskRepository;

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

    @PostMapping(path = "/lessons/{lessonId}/materials")
    public ResponseEntity<Material> saveMaterial(@PathVariable Long lessonId, @RequestBody Material material)
            throws IOException {
        Lesson lesson = repository.findById(lessonId).get();
        material.setId(null);
        material.setLesson(lesson);
        material = materialRepository.save(material);

        return new ResponseEntity<>(material, HttpStatus.OK);
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

    @DeleteMapping(path = "/materials/{materialId}/delete")
    public void deleteMaterial(@PathVariable Long materialId) {
        materialRepository.deleteById(materialId);
    }
}
