package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.models.Course;
import com.example.UniTimeTableManagemend.models.enums.Faculty;
import com.example.UniTimeTableManagemend.respositories.CourseRepository;
import com.example.UniTimeTableManagemend.exception.CourseException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        // get all course data from db
        List<Course> courses = courseRepository.findAll();
        //check datas are exists
        if(courses.size() > 0)
            return courses;
        else
            //if no data in db create an empty array list
            return new ArrayList<Course>();
    }

    public void insertCourse(Course course) throws CourseException, ConstraintViolationException {
        //get the course if already exists in course code
         Optional<Course> course1 = courseRepository.findCourseByCode(course.getCode());
         if(course1.isPresent()){
             System.out.println("Already course code " + course.getCode() + " exist" );
             throw new CourseException(CourseException.AlreadyExists(course.getCode()));
         }else{
             courseRepository.insert(course);
             System.out.println("inserted " + course);
         }
    }

    public void deleteCourse(String courseId) throws CourseException {

        //check given course id exists in db
        if(courseRepository.existsById(courseId)){
            courseRepository.deleteById(courseId);
            System.out.println("Deleted " + courseId);
        }else
            throw new CourseException(CourseException.NotFoundException(courseId));

    }

    public void updateCourse(String courseId, Course course1) throws ConstraintViolationException, CourseException {

        //get the course given by course id else throw
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseException(CourseException.NotFoundException(courseId)));

        //check new and older course id is same
        if(!course1.getCode().equals(course.getCode())){
            //get the course if already in db with course code
            Optional<Course> courseOptional = courseRepository.findCourseByCode(course1.getCode());
            if(courseOptional.isPresent()){
                System.out.println("Already course code " + courseOptional.get().getCode() + " exist" );
                throw new CourseException(CourseException.AlreadyExists(courseOptional.get().getCode()));
            }
            course.setCode(course1.getCode());

        }
        course.setDescription(course1.getDescription());
        course.setName(course1.getName());
        course.setCredit(course1.getCredit());
        courseRepository.save(course);
        System.out.println("Updated " + course);

    }

    public void updateCourseFaculty(String courseId, Course course1) throws CourseException,ConstraintViolationException{
        //find and get course if it is in the db by given course id or throw
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseException(CourseException.NotFoundException(courseId)));

        course.setFaculty(course1.getFaculty());
        courseRepository.save(course);

    }


    //check course by given course code method
    public Boolean findByCourseCode(String code) throws CourseException{
        Optional<Course> course1 = courseRepository.findCourseByCode(code);
        if(course1.isPresent()){
            return true;
        }else{
            throw new CourseException(CourseException.NotFoundException(code));
        }
    }
}
