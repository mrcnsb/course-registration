package com.mts.courseApp.controller;

import com.mts.courseApp.dataTypes.DTO.StudentDTO;
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
@RequestMapping("/students")
@Validated
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity registerNewStudent(@Valid @RequestBody StudentDTO studentDTO) {
        String message = studentService.registerStudent(studentDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/details/{studentId}")
    public ResponseEntity getStudentDetails(@PathVariable("studentId") String studentId) {
        StudentDTO studentDetails = studentService.getStudentDetails(studentId);
        return new ResponseEntity(studentDetails, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity deleteStudent(@PathVariable("studentId") String studentId) {
        String message = studentService.deleteStudent(studentId);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @PatchMapping("/edit/{studentId}")
    public ResponseEntity editStudent(@PathVariable("studentId") String studentId, @RequestBody StudentDTO updatedStudent) {
        String message = studentService.editStudentDetails(studentId, updatedStudent);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @PostMapping("/register/{studentId}/to-course/{courseId}")
    public ResponseEntity registerStudentToCourse(
            @PathVariable("studentId") String studentId,
            @PathVariable("courseId") String courseId) {
        String message = studentService.registerStudentToCourse(studentId, courseId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/unregister/{studentId}/from-course/{courseId}")
    public ResponseEntity<String> unregisterStudentFromCourse(
            @PathVariable("studentId") String studentId,
            @PathVariable("courseId") Long courseId) {
        String message = studentService.unregisterStudentFromCourse(studentId, courseId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/withoutAnyCourses")
    public ResponseEntity getStudentsWithoutAnyCourses() {
        Set<StudentDTO> studentsWhithoutAnyCourses = studentService.getStudentsWhithoutAnyCourse();
        return new ResponseEntity(studentsWhithoutAnyCourses, HttpStatus.OK);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Set<StudentDTO>> getStudentsRegisteredForCourse(@PathVariable String courseId) {
        Set<StudentDTO> students = studentService.getStudentsRegisteredForCourse(courseId);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
}
