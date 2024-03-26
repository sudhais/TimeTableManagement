package com.example.UniTimeTableManagemend.controllers;

import com.example.UniTimeTableManagemend.dto.AuthenticationResponse;
import com.example.UniTimeTableManagemend.dto.RegisterRequest;
import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.TimeTableException;
import com.example.UniTimeTableManagemend.exception.UserException;
import com.example.UniTimeTableManagemend.models.User;
import com.example.UniTimeTableManagemend.services.UserService;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("admin/student/getAll")
    public ResponseEntity<List<User>> getAllStudents(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, users.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping("/auth/student/register")
    public ResponseEntity<?> addNewStudent(@RequestBody RegisterRequest request){
        try {
            return new ResponseEntity<>(userService.addNewUser(request), HttpStatus.OK);
        }catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (UserException | CourseException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/admin/student/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") String userid, @RequestBody User user){

        try {
            userService.updateUser(userid, user);
            return new ResponseEntity<>("successfully updated " + user, HttpStatus.OK);
        }catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (UserException | CourseException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/student/enroll")
    public ResponseEntity<?> addCourseEnrollment(@RequestHeader("Authorization") String token, @RequestParam String courseCode){
        try {
            userService.addCourseEnrollment(token,courseCode);
            return new ResponseEntity<>("successfully enrolled " + courseCode, HttpStatus.OK);
        }catch (UserException | CourseException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/student/deleteEnroll")
    public ResponseEntity<?> deleteCourseEnrollment(@RequestHeader("Authorization") String token, @RequestParam String courseCode){
        try {
            userService.deleteCourseEnrollment(token,courseCode);
            return new ResponseEntity<>("successfully deleted " + courseCode, HttpStatus.OK);
        }catch (UserException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/student/timetable")
    public ResponseEntity<?> getTimetable(@RequestHeader("Authorization") String token){
        try {
            return new ResponseEntity<>(userService.getTimeTable(token), HttpStatus.OK);
        } catch (TimeTableException | CourseException | UserException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }
}
