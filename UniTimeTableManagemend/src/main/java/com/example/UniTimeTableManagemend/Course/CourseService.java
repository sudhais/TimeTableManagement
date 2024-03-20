package com.example.UniTimeTableManagemend.Course;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public void insertCourse(Course course) {

        courseRepository.findCourseByCode(course.getCode())
                .ifPresentOrElse(course1 -> {
                    System.out.println("Already course code " + course1.getCode() + " exist" );
                    throw new IllegalStateException("Already course code " + course1.getCode() + " exist");

                },() -> {
                    courseRepository.insert(course);
                    System.out.println("inserted " + course);
                });
    }

    public void deleteCourse(String courseId) {

        if(!courseRepository.existsById(courseId))
            throw new IllegalStateException("course id does not exists");

        courseRepository.deleteById(courseId);
        System.out.println("Deleted " + courseId);
    }

    public void updateCourse(String courseId, Course course1) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalStateException("course id does not exists"));

        if(course1.getCode() != null &&
                course1.getCode().length() > 0 &&
                !course1.getCode().equals(course.getCode())){

            courseRepository.findCourseByCode(course1.getCode())
                    .ifPresentOrElse(c -> {
                        System.out.println("Already course code " + c.getCode() + " exist" );
                        throw new IllegalStateException("Already course code " + c.getCode() + " exist");

                    },() -> {
                        course.setCode(course1.getCode());
                    });
        }

        if(course1.getName() != null &&
                course1.getName().length() > 0 &&
                course1.getName().equals(course.getName())){

            course.setName(course1.getName());

        }

        if(course1.getDescription() != null &&
                course1.getDescription().length() > 0 &&
                course1.getDescription().equals(course.getDescription())){

            course.setDescription(course1.getDescription());

        }

        if(course1.getCredit() >= 0 && course1.getCredit() <= 4){

            course.setCredit(course1.getCredit());

        }

        courseRepository.save(course);
        System.out.println("Updated " + course);

    }
}
