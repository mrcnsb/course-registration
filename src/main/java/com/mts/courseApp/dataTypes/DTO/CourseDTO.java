package com.mts.courseApp.dataTypes.DTO;

import com.mts.courseApp.dataTypes.entity.Course;
import com.mts.courseApp.dataTypes.entity.Student;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
public class CourseDTO {
    private Long courseId;
    @NotBlank(message = "Course name is mandatory")
    private String courseName;
    private int numOfRegisteredStudents;
    private Set<Student> registeredStudents;

    public CourseDTO(String test_course) {
    }
    public CourseDTO(Long courseId, String courseName, int numOfRegisteredStudents, Set<Student> registeredStudents) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.numOfRegisteredStudents = numOfRegisteredStudents;
        this.registeredStudents = registeredStudents;
    }

    public static CourseDTO from(Course course) {
        return new CourseDTO(course.getCourseId(), course.getCourseName(), course.getNumOfRegisteredStudents(), course.getRegisteredStudents());
    }
}
