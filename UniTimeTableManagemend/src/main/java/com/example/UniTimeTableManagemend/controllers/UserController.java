package com.example.UniTimeTableManagemend.controllers;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.UserException;
import com.example.UniTimeTableManagemend.models.User;
import com.example.UniTimeTableManagemend.services.UserService;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/student")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllStudents(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, users.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> addNewStudent(@RequestBody User user){
        try {
            userService.addNewUser(user);
            return new ResponseEntity<>("successfully inserted " + user, HttpStatus.OK);
        }catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (UserException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }catch (CourseException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PostMapping(path = "{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") String userid, @RequestBody User user){

        try {
            userService.updateUser(userid, user);
            return new ResponseEntity<>("successfully inserted " + user, HttpStatus.OK);
        }catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (UserException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }catch (CourseException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }
}
