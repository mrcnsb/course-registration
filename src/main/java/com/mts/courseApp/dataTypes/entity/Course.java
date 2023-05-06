package com.mts.courseApp.dataTypes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private Long courseId;
    private String courseName;
    @JsonIgnore
    @ManyToMany
            (
                    fetch = FetchType.EAGER,
                    cascade = CascadeType.PERSIST,
                    mappedBy = "registeredCourses"
            )
    private Set<Student> registeredStudents = new HashSet<>();
    private int numOfRegisteredStudents;

    public Course(String courseName) {
        this.courseName = courseName;
    }
}
