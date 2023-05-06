package com.mts.courseApp.controller;

import com.mts.courseApp.dataTypes.DTO.CourseDTO;
import com.mts.courseApp.service.CourseService;
import com.mts.courseApp.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
@Validated
public class CourseController {
    private final CourseService courseService;
    private final StudentService studentService;

    @PostMapping("/add")
    public ResponseEntity addNewCourse(@Valid @RequestBody CourseDTO courseDTO) {
        String message = courseService.addNewCourse(courseDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/details/{courseId}")
    public ResponseEntity getCourseDetails(@PathVariable("courseId") String courseId) {
        CourseDTO courseDetails = courseService.getCourseDetails(courseId);
        return new ResponseEntity(courseDetails, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity deleteCourse(@PathVariable("courseId") String courseId) {
        String message = courseService.deleteCourse(courseId);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @PatchMapping("/edit/{courseId}")
    public ResponseEntity editCourse(@PathVariable("courseId") String courseId, CourseDTO updatedCourse) {
        String message = courseService.editCourse(courseId, updatedCourse);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @GetMapping("/withoutAnyStudents")
    public ResponseEntity getCoursesWithoutAnyStudents(){
        Set<CourseDTO> coursesWithoutAnyStudents = courseService.getCoursesWithoutAnyStudents();
        return new ResponseEntity(coursesWithoutAnyStudents, HttpStatus.OK);
    }

    @GetMapping("/filteredBySpecificStudent/{studentId}")
    public ResponseEntity<Set<CourseDTO>> getCoursesRegisteredByStudent(@PathVariable String studentId) {
        Set<CourseDTO> courses = studentService.getCoursesRegisteredByStudent(studentId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }
}
