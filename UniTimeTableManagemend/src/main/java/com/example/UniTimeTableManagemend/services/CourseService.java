package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.models.Course;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

public interface CourseService {

    public List<Course> getAllCourses();
    public Course insertCourse(Course course) throws CourseException, ConstraintViolationException;
    public Boolean deleteCourse(String courseId) throws CourseException;
    public Course updateCourse(String courseId, Course course1) throws ConstraintViolationException, CourseException;
    public Course updateCourseFaculty(String courseId, Course course1) throws CourseException,ConstraintViolationException;
    public Course findByCourseCode(String code) throws CourseException;
}
