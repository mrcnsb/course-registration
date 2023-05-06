package com.mts.courseApp.dataTypes.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mts.courseApp.dataTypes.entity.Course;
import com.mts.courseApp.dataTypes.entity.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"email"})})
public class StudentDTO {
    private String studentId;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Surname is mandatory")
    private String surname;
    @Column(unique = true)
    @Email
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "Password is mandatory")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private Set<Course> registeredCourses = new HashSet<>();
    private int numberOfRegisteredCourses;

    public StudentDTO(String studentId, String name, String surname, String email, Set<Course> registeredCourses, int numberOfRegisteredCourses) {
        this.studentId = studentId;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.registeredCourses = registeredCourses;
        this.numberOfRegisteredCourses = numberOfRegisteredCourses;
    }

    public StudentDTO(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public static StudentDTO from(Student student) {
        return new StudentDTO(student.getStudentId(), student.getName(), student.getSurname(), student.getEmail(), student.getRegisteredCourses(), student.getNumOfRegisteredCourses());
    }
}
