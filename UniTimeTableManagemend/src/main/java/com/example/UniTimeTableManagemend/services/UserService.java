package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.dto.AuthenticationResponse;
import com.example.UniTimeTableManagemend.dto.EnrollmentResponse;
import com.example.UniTimeTableManagemend.dto.RegisterRequest;
import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.TimeTableException;
import com.example.UniTimeTableManagemend.exception.UserException;
import com.example.UniTimeTableManagemend.models.TimeTable;
import com.example.UniTimeTableManagemend.models.User;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers();
    public AuthenticationResponse addNewUser(RegisterRequest request) throws ConstraintViolationException, UserException, CourseException;
    public void updateUser(String userid, User user) throws UserException, ConstraintViolationException, CourseException;
    public User findUserByEmail(String email);
    public TimeTable getTimeTable(String token) throws TimeTableException, CourseException, UserException;
    public void addCourseEnrollment(String token, String courseCode) throws UserException, CourseException;
    public void deleteCourseEnrollment(String token, String courseCode) throws UserException;
    public EnrollmentResponse getStudentEnrollDetails(String email) throws UserException;
    public EnrollmentResponse updateStudentEnrollDetails(EnrollmentResponse enroll) throws UserException, CourseException;
}
