package com.example.demo.student;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Entity
@Table
public class Student {
    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private Long id;
    @NotNull(message = "Name is required")
    private String name;
    @Column(unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    private String url = "https://picsum.photos/200";
    @Transient
    private Integer age;

    public Student() {
    }

    public Student(Long id, String name, String email, LocalDate dob, String url) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.url = url;
    }

    public Student(String name, String email, LocalDate dob, String url) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Integer getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name=" + name +
                ", email=" + email +
                ", dob=" + dob +
                ", url=" + url +
                '}';
    }

    public List<Student> toArray() {
        return List.of(this);
    }
}
