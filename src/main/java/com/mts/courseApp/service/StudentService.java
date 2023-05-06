package com.mts.courseApp.service;

import com.mts.courseApp.dataTypes.DTO.CourseDTO;
import com.mts.courseApp.dataTypes.DTO.StudentDTO;
import com.mts.courseApp.dataTypes.entity.Course;
import com.mts.courseApp.dataTypes.entity.Student;
import com.mts.courseApp.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    @Value("${maximumNumberOfCoursesRegistered}")
    private int maxNumOfCourses;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final CourseService courseService;

    public String registerStudent(StudentDTO studentDTO) {
        isEmailAlreadyInUse(studentDTO.getEmail());
        studentRepository.saveAndFlush(studentFromDTO(studentDTO));
        return "Student registered";
    }

    public StudentDTO getStudentDetails(String studentId) {
        return StudentDTO.from(findStudentById(studentId));
    }

    public String deleteStudent(String studentId) {
        Student student = findStudentById(studentId);
        deleteStudentFromCourses(student);
        studentRepository.delete(student);
        return "Succesfully deleted Student with ID: " + studentId;
    }

    public String editStudentDetails(String studentId, StudentDTO updatedStudent) {
        Student student = findStudentById(studentId);
        if (updatedStudent.getName() != null) {
            student.setName(updatedStudent.getName());
        }
        if (updatedStudent.getSurname() != null) {
            student.setSurname(updatedStudent.getSurname());
        }
        if (updatedStudent.getEmail() != null) {
            student.setEmail(updatedStudent.getEmail());
        }
        if (updatedStudent.getPassword() != null) {
            student.setPassword(passwordEncoder.encode(updatedStudent.getPassword()));
        }
        studentRepository.saveAndFlush(student);
        return "Student with ID " + studentId + " has been successfully updated.";
    }

    public String registerStudentToCourse(String studentId, String courseId) {
        Student student = findStudentById(studentId);
        Course course = courseService.findCourseById(courseId);
        checkRequirementsAndRegister(student, course);
        return "Student with ID " + studentId + " has been successfully registered to the course with ID " + courseId + ".";
    }

    public String unregisterStudentFromCourse(String studentId, Long courseId) {
        Student student = findStudentById(studentId);
        Course course = courseService.findCourseById(String.valueOf(courseId));
        if (!student.getRegisteredCourses().contains(course)) {
            throw new IllegalStateException("Student is not registered for the course.");
        }
        student.getRegisteredCourses().remove(course);
        student.setNumOfRegisteredCourses(student.getNumOfRegisteredCourses() - 1);
        studentRepository.save(student);
        courseService.editCourseAfterDeletingStudent(course, student);
        return "Student successfully unregistered from the course.";
    }

    public Set<StudentDTO> getStudentsWhithoutAnyCourse() {
        Set<Student> students = studentRepository.findAllByNumOfRegisteredCourses(0);
        return students.stream()
                .map(StudentDTO::from)
                .collect(Collectors.toSet());
    }

    public Set<StudentDTO> getStudentsRegisteredForCourse(String courseId) {
        Course course = courseService.findCourseById(courseId);
        Set<Student> registeredStudents = course.getRegisteredStudents();
        return registeredStudents.stream()
                .map(StudentDTO::from)
                .collect(Collectors.toSet());
    }

    public Set<CourseDTO> getCoursesRegisteredByStudent(String studentId) {
        Student student = findStudentById(studentId);
        Set<Course> registeredCourses = student.getRegisteredCourses();
        return registeredCourses.stream()
                .map(CourseDTO::from)
                .collect(Collectors.toSet());
    }

    private Student findStudentById(String studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("There is no student with ID: " + studentId));
    }

    private Student studentFromDTO(StudentDTO studentDTO) {
        return new Student(
                studentDTO.getName(),
                studentDTO.getSurname(),
                studentDTO.getEmail(),
                passwordEncoder.encode(studentDTO.getPassword()),
                studentDTO.getNumberOfRegisteredCourses());
    }

    private void checkRequirementsAndRegister(Student student, Course course) {
        courseService.checkAvailablePlaces(course);
        if (student.getNumOfRegisteredCourses() < maxNumOfCourses && checkIfAlreadyRegistered(student, course)) {
            student.setNumOfRegisteredCourses(student.getNumOfRegisteredCourses() + 1);
            student.getRegisteredCourses().add(course);
            studentRepository.saveAndFlush(student);
        } else
            throw new IndexOutOfBoundsException
                    ("You are already registered to " + student.getNumOfRegisteredCourses() + "/" + maxNumOfCourses + " courses");
    }

    private boolean checkIfAlreadyRegistered(Student student, Course course) {
        if (!student.getRegisteredCourses().contains(course))
            return true;
        throw new IllegalArgumentException("You are already registered to this course.");
    }
    private void isEmailAlreadyInUse(String email) {
        if(studentRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Student with email: " + email + " already exists");
        }
    }

    private void deleteStudentFromCourses(Student student) {
        Set<Course> registeredCourses = student.getRegisteredCourses();
        for (Course course : registeredCourses) {
            course.getRegisteredStudents().remove(student);
            course.setNumOfRegisteredStudents(course.getNumOfRegisteredStudents() - 1);
        }
        student.setRegisteredCourses(Collections.emptySet());
        studentRepository.saveAndFlush(student);
    }
}

