package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.models.Course;
import com.example.UniTimeTableManagemend.models.enums.Faculty;
import com.example.UniTimeTableManagemend.respositories.CourseRepository;
import com.example.UniTimeTableManagemend.services.Impl.CourseServiceImp;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImp courseService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        courseService = new CourseServiceImp(courseRepository);
    }

    @Test
    void getAllCourses_Success() {
        List<Course> expectedCourses = new ArrayList<>();
        Course course = init_Course();
        expectedCourses.add(course);
        when(courseRepository.findAll()).thenReturn(expectedCourses);

        List<Course> actualCourses = courseService.getAllCourses();

        assertNotNull(actualCourses);
        assertEquals(expectedCourses.size(),actualCourses.size());
        assertEquals(expectedCourses.get(0),actualCourses.get(0));

    }

    @Test
    void getAllCourses_noDataInRepository_emptyListReturned() {
        when(courseRepository.findAll()).thenReturn(new ArrayList<>());

        List<Course> courses = courseService.getAllCourses();

        assertNotNull(courses);
        assertTrue(courses.isEmpty());
    }

    @Test
    void insertCourse_courseDoesNotExist_courseInserted() throws CourseException, ConstraintViolationException {
        Course course = new Course("C001", "Course 1", "Description 1", 3, Faculty.IT );

        when(courseRepository.findCourseByCode("C001")).thenReturn(Optional.empty());

        Course insertedCourse = courseService.insertCourse(course);

        assertNotNull(insertedCourse);
        assertEquals(course, insertedCourse);
    }

    @Test
    void insertCourse_courseAlreadyExists_exceptionThrown() throws CourseException {

        Course existCourse = new Course("SE2030","DS","what",7,Faculty.ENGINEERING);
        Course newCourse = new Course("SE2030","DS","what",7,Faculty.ENGINEERING);

        when(courseRepository.findCourseByCode("SE2030")).thenReturn(Optional.of(existCourse));

        // Call the service method and expect CourseException
        assertThrows(CourseException.class, () -> courseService.insertCourse(newCourse));

        // Verify that insert method is not called
        verify(courseRepository, never()).insert(newCourse);


    }

    @Test
    public void deleteCourse_Success() throws CourseException {
        when(courseRepository.existsById("1")).thenReturn(true);

        Boolean actual = courseService.deleteCourse("1");

        // Verify that deleteById method is called with correct argument
        verify(courseRepository).deleteById("1");

        assertTrue(actual);
    }

    @Test
    public void deleteCourse_NotExists() throws CourseException {
        when(courseRepository.existsById("2")).thenReturn(false);

        // Call the service method and expect CourseException
        assertThrows(CourseException.class, ()-> courseService.deleteCourse("2"));

        // Verify that deleteById method is not called
        verify(courseRepository, never()).deleteById("2");
    }

    @Test
    void updateCourse_Success() throws CourseException {
        String id = "1";
        Course existingCourse = init_Course();
        Course updatedCourse = new Course(
                "SE3010",
                "DS",
                "sddfas",
                2,
                Faculty.IT
        );
        updatedCourse.setName("IP");
        when(courseRepository.findById(id)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(existingCourse)).thenReturn(existingCourse);

        Course updated = courseService.updateCourse(id,updatedCourse);

        assertNotNull(updated);
        assertEquals(updated.getName(),updated.getName());
    }

    @Test
    void updateCourse_CourseIdNotExists_exceptionThrown() {
        // Mock data
        String courseId = "123";
        Course course1 = new Course("CS101", "Introduction to Computer Science", "An introductory course to computer science", 3, null);

        // Stubbing the repository method to return Optional.empty() since course ID does not exist
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Call the service method and expect CourseException
        assertThrows(CourseException.class, () -> {
            courseService.updateCourse(courseId, course1);
        });

        // Verify that findById method is called with the correct argument
        verify(courseRepository).findById(courseId);

        // Verify that save method is not called
        verify(courseRepository, never()).save(any());
    }


    @Test
    void updateCourseFaculty_courseExists_facultyUpdated() throws CourseException, ConstraintViolationException {
        String courseId = "C001";
        Course existingCourse = new Course(courseId, "Course 1", "Description 1", 3,Faculty.IT);
        Course updatedCourse = new Course(courseId, "Course 1", "Description 1",  3, Faculty.ENGINEERING);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));

        assertDoesNotThrow(() -> courseService.updateCourseFaculty(courseId, updatedCourse));
        assertEquals(updatedCourse.getFaculty(), existingCourse.getFaculty());
    }

    @Test
    void findByCourseCode_courseExists_courseReturned() throws CourseException {
        String courseCode = "C001";
        Course course = new Course(courseCode, "Course 1", "Description 1", 3, Faculty.ENGINEERING);

        when(courseRepository.findCourseByCode(courseCode)).thenReturn(Optional.of(course));

        Course foundCourse = courseService.findByCourseCode(courseCode);

        assertNotNull(foundCourse);
        assertEquals(course, foundCourse);
    }

    @Test
    void findByCourseCode_courseDoesNotExist_exceptionThrown() {
        String courseCode = "C001";

        when(courseRepository.findCourseByCode(courseCode)).thenReturn(Optional.empty());

        assertThrows(CourseException.class, () -> courseService.findByCourseCode(courseCode));
    }

    private Course init_Course(){
        Course course = new Course(
                "SE3020",
                "AF",
                "testing ",
                4,
                Faculty.IT
        );
        course.setId("1");
        return course;
    }

}