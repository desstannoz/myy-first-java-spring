package com.example.demo.student;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("${apiPrefix}/student")
public class StudentController {
    private final StudentService studentService;
    @Autowired
    StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @GetMapping
    public List<Student> getStudent() {
        return studentService.getStudents();
    }

    @GetMapping("{id}")
    public Student Student(@PathVariable("id") Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping
    public List<Student> addNewStudent(@Valid @RequestBody Object student) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        if(student instanceof List) {
            List<Student> studentList = mapper.convertValue(student, new TypeReference<List<Student>>() {});
            return studentService.addNewStudent(studentList);
        } else {
            Student student1 = mapper.convertValue(student, Student.class);
            return studentService.addNewStudent(student1);
        }
    }
}
