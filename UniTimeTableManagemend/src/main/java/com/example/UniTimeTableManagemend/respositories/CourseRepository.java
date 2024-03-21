package com.example.UniTimeTableManagemend.respositories;

import com.example.UniTimeTableManagemend.models.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course,String> {

    Optional<Course> findCourseByCode(String code);
}
