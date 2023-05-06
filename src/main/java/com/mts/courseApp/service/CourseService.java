package com.mts.courseApp.service;

import com.mts.courseApp.dataTypes.DTO.CourseDTO;
import com.mts.courseApp.dataTypes.entity.Course;
import com.mts.courseApp.dataTypes.entity.Student;
import com.mts.courseApp.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    @Value("${maximumNumberOfStudentsRegistered}")
    private int maxNumOfStudents;
    private final CourseRepository courseRepository;

    public String addNewCourse(CourseDTO courseDTO) {
        checkIfCourseNameIsAlreadyInUse(courseDTO);
        courseRepository.saveAndFlush(courseFromDTO(courseDTO));
        return "Course succesfully added.";
    }


    public CourseDTO getCourseDetails(String courseId) {
        return CourseDTO.from(findCourseById(courseId));
    }

    public String deleteCourse(String courseId) {
        Course course = findCourseById(courseId);
        if (course.getNumOfRegisteredStudents() == 0)
            courseRepository.deleteById(courseId);
        else
            throw new RuntimeException("You cannot delete course with registered students. Please unregister students first.");
        return "Succesfully deleted course with ID:" + courseId;
    }

    public String editCourse(String courseId, CourseDTO updatedCourse) {
        Course course = findCourseById(courseId);
        if (updatedCourse.getCourseName() != null) {
            course.setCourseName(updatedCourse.getCourseName());
        }
        courseRepository.saveAndFlush(course);
        return "Course with ID " + courseId + " has been successfully updated.";
    }

    public Course findCourseById(String courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("There is no course with ID:" + courseId));
    }

    public void checkAvailablePlaces(Course course) {
        if (course.getNumOfRegisteredStudents() < maxNumOfStudents) {
            course.setNumOfRegisteredStudents(course.getNumOfRegisteredStudents() + 1);
            courseRepository.saveAndFlush(course);
        } else
            throw new IndexOutOfBoundsException
                    ("This course has no more available places");
    }

    public Set<CourseDTO> getCoursesWithoutAnyStudents() {
        Set<Course> courses = courseRepository.findAllBynumOfRegisteredStudents(0);
        return courses.stream()
                .map(CourseDTO::from)
                .collect(Collectors.toSet());
    }

    public void editCourseAfterDeletingStudent(Course course, Student student) {
        course.getRegisteredStudents().remove(student);
        course.setNumOfRegisteredStudents(course.getNumOfRegisteredStudents() - 1);
        courseRepository.saveAndFlush(course);
    }

    private Course courseFromDTO(CourseDTO courseDTO) {
        return new Course(courseDTO.getCourseName());
    }

    private void checkIfCourseNameIsAlreadyInUse(CourseDTO courseDTO) {
        if (courseRepository.findByCourseName(courseDTO.getCourseName()).isPresent()) {
            throw new RuntimeException("Course with this name: " + courseDTO.getCourseName() + " already exists");
        }
    }
}
