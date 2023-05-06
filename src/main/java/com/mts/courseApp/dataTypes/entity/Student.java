package com.mts.courseApp.dataTypes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "uuid2")
    private String studentId;
    private String name;
    private String surname;
    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    @ManyToMany
            (
                    fetch = FetchType.EAGER,
                    cascade = CascadeType.MERGE
            )
    @JoinTable
            (
                    name = "student_course_set",
                    joinColumns = @JoinColumn(name = "studentId"),
                    inverseJoinColumns = @JoinColumn(name = "courseId"),
                    uniqueConstraints = @UniqueConstraint(columnNames = {
                            "studentId", "courseId"})
            )
    private Set<Course> registeredCourses = new HashSet<>();
    private int numOfRegisteredCourses;

    public Student(String name, String surname, String email, String password, int numOfRegisteredCourses) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.numOfRegisteredCourses = numOfRegisteredCourses;
    }

    public Student(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }
}
