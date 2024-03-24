package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.models.Course;
import com.example.UniTimeTableManagemend.models.enums.Faculty;
import com.example.UniTimeTableManagemend.respositories.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    private CourseService courseService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        courseService = new CourseService(courseRepository);
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
    void insertCourse_Success() {

        try {
            Course course = init_Course();
            when(courseRepository.save(course)).thenReturn(course);
            Course createdCourse = courseService.insertCourse(course);

            assertNotNull(createdCourse);
            assertEquals(course,createdCourse);
        } catch (CourseException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void deleteCourse_Success() throws CourseException {
        when(courseRepository.existsById("1")).thenReturn(true);

        Boolean actual = courseService.deleteCourse("1");
        assertTrue(actual);
    }

    @Test
    void updateCourse_Success() throws CourseException {
        String id = "1";
        Course existingCourse = init_Course();
        Course updatedCourse = new Course(
                "SE3020",
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

    private Course init_Course(){
        Course course = new Course(
                "SE3020",
                "AF",
                "testing ",
                4,
                Faculty.IT
        );
        return course;
    }

}