package com.example.UniTimeTableManagemend.respositories;

import com.example.UniTimeTableManagemend.models.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends MongoRepository<Course,String> {

    Optional<Course> findCourseByCode(String code);
}
