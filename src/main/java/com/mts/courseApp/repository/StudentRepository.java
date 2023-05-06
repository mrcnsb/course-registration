package com.mts.courseApp.repository;

import com.mts.courseApp.dataTypes.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    Set<Student> findAllByNumOfRegisteredCourses(int i);
    Optional findByEmail(String email);
}
