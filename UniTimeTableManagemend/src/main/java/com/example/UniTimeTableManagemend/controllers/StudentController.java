package com.example.UniTimeTableManagemend.controllers;

import com.example.UniTimeTableManagemend.models.Student;
import com.example.UniTimeTableManagemend.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/student")
@AllArgsConstructor
public class StudentController {

    private StudentService studentService;

    @GetMapping
    public ResponseEntity<?> getAllCourses(){
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, students.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
