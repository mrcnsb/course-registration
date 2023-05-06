package com.mts.courseApp.service;

import com.mts.courseApp.dataTypes.DTO.CourseDTO;
import com.mts.courseApp.dataTypes.entity.Course;
import com.mts.courseApp.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CourseServiceTest {
    @Autowired
    private CourseService courseService;
    @MockBean
    private CourseRepository courseRepository;

    @Test
    void addNewCourseTest() {
        CourseDTO courseDTO = new CourseDTO("Test Course");
        Course course = new Course(courseDTO.getCourseName());
        when(courseRepository.saveAndFlush(any(Course.class))).thenReturn(course);
        String result = courseService.addNewCourse(courseDTO);
        assertEquals("Course succesfully added.", result);
    }

    @Test
    void getCourseDetailsTest() {
        Long courseId = 1L;
        Course course = new Course("Test Course");
        course.setCourseId(courseId);
        CourseDTO expectedCourseDTO = CourseDTO.from(course);
        when(courseRepository.findById(String.valueOf(courseId))).thenReturn(Optional.of(course));
        CourseDTO result = courseService.getCourseDetails(String.valueOf(courseId));
        assertEquals(expectedCourseDTO, result);
    }

    @Test
    void deleteCourseTest() {
        Long courseId = 1L;
        Course course = new Course("Test Course");
        course.setCourseId(courseId);
        when(courseRepository.findById(String.valueOf(courseId))).thenReturn(Optional.of(course));
        courseService.deleteCourse(String.valueOf(courseId));
        verify(courseRepository, times(1)).deleteById(String.valueOf(courseId));
    }

    @Test
    void editCourseTest() {
        Long courseId = 1L;
        Course course = new Course("Test Course");
        course.setCourseId(courseId);
        CourseDTO updatedCourseDTO = new CourseDTO("Test Course");
        when(courseRepository.findById(String.valueOf(courseId))).thenReturn(Optional.of(course));
        when(courseRepository.saveAndFlush(any(Course.class))).thenAnswer(invocation -> {
            Course updatedCourse = invocation.getArgument(0);
            course.setCourseName(updatedCourse.getCourseName());
            return course;
        });
        String result = courseService.editCourse(String.valueOf(courseId), updatedCourseDTO);
        assertEquals("Course with ID " + courseId + " has been successfully updated.", result);
        assertEquals("Test Course", course.getCourseName());
    }

    @Test
    void findCourseByIdTest() {
        Long courseId = 1L;
        Course course = new Course("Test Course");
        course.setCourseId(courseId);
        when(courseRepository.findById(String.valueOf(course.getCourseId()))).thenReturn(Optional.of(course));
        Course result = courseService.findCourseById(String.valueOf(courseId));
        assertEquals(course, result);
    }

    @Test
    void checkAvailablePlacesTest() {
        Long courseId = 1L;
        Course course = new Course("Test Course");
        course.setCourseId(courseId);
        course.setNumOfRegisteredStudents(5);
        when(courseRepository.findById(String.valueOf(course.getCourseId()))).thenReturn(Optional.of(course));
        when(courseRepository.saveAndFlush(any(Course.class))).thenReturn(course);
        courseService.checkAvailablePlaces(course);
        assertEquals(6, course.getNumOfRegisteredStudents());
    }

    @Test
    void getCoursesWithoutAnyStudentsTest() {
        Course course1 = new Course("Test Course 1");
        course1.setCourseId(1L);
        course1.setNumOfRegisteredStudents(0);
        Course course2 = new Course("Test Course 2");
        course2.setCourseId(2L);
        course2.setNumOfRegisteredStudents(0);
        Set<Course> courses = new HashSet<>();
        courses.add(course1);
        courses.add(course2);
        when(courseRepository.findAllBynumOfRegisteredStudents(0)).thenReturn(courses);
        Set<CourseDTO> result = courseService.getCoursesWithoutAnyStudents();
        assertEquals(2, result.size());
        assertTrue(result.contains(CourseDTO.from(course1)));
        assertTrue(result.contains(CourseDTO.from(course2)));
    }
}
