package com.example.UniTimeTableManagemend;

import com.example.UniTimeTableManagemend.Course.Course;
import com.example.UniTimeTableManagemend.Course.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class UniTimeTableManagemendApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniTimeTableManagemendApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runner(CourseRepository courseRepository){
//		return args -> {
//			Course course = new Course("SE3010","AF","what to do", 4);
//			courseRepository.insert(course);
//		};
//	}

}
