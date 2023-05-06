package com.mts.courseApp.service;

import com.mts.courseApp.dataTypes.DTO.CourseDTO;
import com.mts.courseApp.dataTypes.DTO.StudentDTO;
import com.mts.courseApp.dataTypes.entity.Course;
import com.mts.courseApp.dataTypes.entity.Student;
import com.mts.courseApp.repository.CourseRepository;
import com.mts.courseApp.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class StudentServiceTest {
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseService courseService;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private CourseRepository courseRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void registerStudentTest() {
        StudentDTO studentDTO =
                new StudentDTO("Brad", "Malkovich", "mal@example.com", "password");
        Student student =
                new Student("Brad", "Malkovich", "mal@example.com", "encodedPassword", 0);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(studentRepository.saveAndFlush(any(Student.class))).thenReturn(student);
        String result = studentService.registerStudent(studentDTO);
        assertThat(result).isEqualTo("Student registered");
        verify(studentRepository, times(1)).saveAndFlush(any(Student.class));
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    public void getStudentDetailsTest() {
        String studentId = "123456";
        Student student =
                new Student("Angelina", "Jolie", "angelina@example.com", "encodedPassword");
        student.setStudentId(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        StudentDTO studentDTO = studentService.getStudentDetails(studentId);
        assertThat(studentDTO.getName()).isEqualTo(student.getName());
        assertThat(studentDTO.getSurname()).isEqualTo(student.getSurname());
        assertThat(studentDTO.getEmail()).isEqualTo(student.getEmail());
        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    public void editStudentDetailsTest() {
        Student existingStudent =
                new Student("John", "Smith", "John@example.com", "password");
        existingStudent.setStudentId("1");
        StudentDTO updatedStudent =
                new StudentDTO("Martin", "Pitt", "martin@example.com", "password");
        when(studentRepository.findById("1")).thenReturn(java.util.Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));
        String result = studentService.editStudentDetails("1", updatedStudent);
        assertThat(existingStudent.getSurname()).isEqualTo("Pitt");
        assertThat(existingStudent.getEmail()).isEqualTo("martin@example.com");
        assertThat(result).isEqualTo("Student with ID 1 has been successfully updated.");
    }

    @Test
    public void getStudentsWhithoutAnyCourseTest() {
        Student student1 =
                new Student("Emily", "Colins", "emily@example.com", "password", 0);
        student1.setStudentId("1");
        Student student2 =
                new Student("Jane", "Fonda", "jane@example.com", "password", 0);
        student2.setStudentId("2");
        Set<Student> studentSet = new HashSet<>();
        studentSet.add(student1);
        studentSet.add(student2);
        when(studentRepository.findAllByNumOfRegisteredCourses(0)).thenReturn(studentSet);
        Set<StudentDTO> studentsWithoutCourses = studentService.getStudentsWhithoutAnyCourse();
        assertThat(studentsWithoutCourses).isNotEmpty();
        assertThat(studentsWithoutCourses).hasSize(2);
    }

    @Test
    public void getStudentsRegisteredForCourseTest() {
        String courseId = "1";
        Set<Student> registeredStudents = new HashSet<>();
        Student student1 =
                new Student("Andrew", "Damn", "andrew@example.com", "password", 1);
        student1.setStudentId("1");
        registeredStudents.add(student1);
        Course course = new Course("Test Course");
        course.setRegisteredStudents(registeredStudents);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        Set<StudentDTO> studentsRegisteredForCourse = studentService.getStudentsRegisteredForCourse(courseId);
        assertThat(studentsRegisteredForCourse).isNotEmpty();
        assertThat(studentsRegisteredForCourse).hasSize(1);
    }

    @Test
    public void deleteStudentTest() {
        String studentId = "1";
        Student student =
                new Student("John", "Nowak", "john.nowak@example.com", "password", 0);
        student.setStudentId(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        String result = studentService.deleteStudent(studentId);
        assertThat(result).isEqualTo("Succesfully deleted Student with ID: " + studentId);
    }

    @Test
    public void registerStudentToCourseTest() {
        String studentId = "1";
        Long courseId = 1L;
        Student student =
                new Student("Adam", "Smith", "adam@example.com", "password", 0);
        Course course = new Course("Test Course");
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(String.valueOf(courseId))).thenReturn(Optional.of(course));
        String result = studentService.registerStudentToCourse(studentId, String.valueOf(courseId));
        assertThat(result).isEqualTo("Student with ID 1 has been successfully registered to the course with ID 1.");
        assertThat(student.getNumOfRegisteredCourses()).isEqualTo(1);
        assertThat(student.getRegisteredCourses()).contains(course);
    }

    @Test
    public void unregisterStudentFromCourseTest() {
        String studentId = "1";
        Long courseId = 1L;
        Student student =
                new Student("Pamela", "Anderson", "pamela@example.com", "password", 1);
        Course course = new Course("Test Course");
        Set<Course> registeredCourses = student.getRegisteredCourses();
        registeredCourses.add(course);
        studentRepository.saveAndFlush(student);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(String.valueOf(courseId))).thenReturn(Optional.of(course));
        String result = studentService.unregisterStudentFromCourse(studentId, courseId);
        assertThat(result).isEqualTo("Student successfully unregistered from the course.");
        assertThat(student.getNumOfRegisteredCourses()).isEqualTo(0);
    }

    @Test
    public void getCoursesRegisteredByStudentTest() {
        String studentId = "1";
        Student student =
                new Student("Dude", "Man", "dude@example.com", "password", 1);
        student.setStudentId(studentId);
        Course course1 = new Course("Course 1");
        course1.setCourseId(1L);
        Course course2 = new Course("Course 2");
        course2.setCourseId(2L);
        Set<Course> registeredCourses = new HashSet<>();
        registeredCourses.add(course1);
        registeredCourses.add(course2);
        student.setRegisteredCourses(registeredCourses);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        Set<CourseDTO> coursesRegisteredByStudent = studentService.getCoursesRegisteredByStudent(studentId);
        assertThat(coursesRegisteredByStudent).isNotEmpty();
        assertThat(coursesRegisteredByStudent).hasSize(2);
        assertThat(coursesRegisteredByStudent.stream().map(CourseDTO::getCourseName).collect(Collectors.toSet()))
                .containsExactlyInAnyOrder(course1.getCourseName(), course2.getCourseName());
        verify(studentRepository, times(1)).findById(studentId);
    }
}


