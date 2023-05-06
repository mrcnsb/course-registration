package com.mts.courseApp.repository;

import com.mts.courseApp.dataTypes.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    Set<Course> findAllBynumOfRegisteredStudents(int i);
    Optional<Object> findByCourseName(String courseName);
}
