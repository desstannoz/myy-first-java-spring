package com.example.demo.student;

import com.example.demo.service.FilesStorageService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/api/v1/student")
public class StudentController {
    private final StudentService studentService;
    @Autowired
    FilesStorageService storageService;

    @Autowired
    StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("image") MultipartFile file) {
        String message = "";
        try {
            storageService.save(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }
    @GetMapping("/files")
    public ResponseEntity getListFiles() {
        List<String> fileInfos = storageService.loadAll().map(path -> {
            String url = MvcUriComponentsBuilder
                    .fromMethodName(StudentController.class, "getFile", path.getFileName().toString()).build().toString();
            return url;
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    @GetMapping
    public List<Student> getStudent() {
        return studentService.getStudents();
    }

    @GetMapping("{id}")
    public Student Student(@PathVariable("id") Long id) {
        return studentService.getStudentById(id);
    }

    @PatchMapping("{id}")
    public Student updateStudent(@PathVariable("id") Long id, @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }


    @PostMapping
    public List<Student> addNewStudent(@Valid @RequestBody Object student) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        if (student instanceof List) {
            List<Student> studentList = mapper.convertValue(student, new TypeReference<List<Student>>() {
            });
            return studentService.addNewStudent(studentList);
        } else {
            Student student1 = mapper.convertValue(student, Student.class);
            return studentService.addNewStudent(student1);
        }
    }
}
