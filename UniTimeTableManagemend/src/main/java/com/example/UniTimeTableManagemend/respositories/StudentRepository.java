package com.example.UniTimeTableManagemend.respositories;

import com.example.UniTimeTableManagemend.models.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student,String> {
}
