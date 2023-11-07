package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new IllegalStateException("Student with id " + id + " does not exist"));
    }

    public List<Student> addNewStudent(Object student) {
        if(student instanceof List) {
            for (Student s : (List<Student>) student) {
                if (studentRepository.findStudentByEmail(s.getEmail()).isPresent()) {
                    throw new IllegalStateException("Email already taken");
                }
            }
            return studentRepository.saveAll((List<Student>) student);
        } else {
            if (studentRepository.findStudentByEmail(((Student) student).getEmail()).isPresent()) {
                throw new IllegalStateException("Email already taken");
            }
            return studentRepository.save((Student) student).toArray();
        }
    }
}
