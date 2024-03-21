package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.models.Student;
import com.example.UniTimeTableManagemend.respositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    public List<Student> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        if(students.size() > 0)
            return students;
        else
            return new ArrayList<Student>();
    }
}
