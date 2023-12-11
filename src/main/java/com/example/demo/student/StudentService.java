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
        // sort by id in descending order
        List<Student> students = studentRepository.findAll();
        students.sort((s1, s2) -> s1.getId().compareTo(s2.getId()));
        return students;
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

    public Student updateStudent(Long id, Student student) {
        Student student1 = studentRepository.findById(id).orElseThrow(() -> new IllegalStateException("Student with id " + id + " does not exist"));
        if(student.getName() != null && student.getName().length() > 0 && !student.getName().equals(student1.getName())) {
            student1.setName(student.getName());
        }
        if(student.getEmail() != null && student.getEmail().length() > 0 && !student.getEmail().equals(student1.getEmail())) {
            if (studentRepository.findStudentByEmail(student.getEmail()).isPresent()) {
                throw new IllegalStateException("Email already taken");
            }
            student1.setEmail(student.getEmail());
        }
        if(student.getDob() != null && !student.getDob().equals(student1.getDob())) {
            student1.setDob(student.getDob());
        }
        if(student.getUrl() != null && student.getUrl().length() > 0 && !student.getUrl().equals(student1.getUrl())) {
            student1.setUrl(student.getUrl());
        }
        return studentRepository.save(student1);
    }
}
