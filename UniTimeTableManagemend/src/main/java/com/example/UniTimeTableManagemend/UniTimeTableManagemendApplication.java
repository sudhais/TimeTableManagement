package com.example.UniTimeTableManagemend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
