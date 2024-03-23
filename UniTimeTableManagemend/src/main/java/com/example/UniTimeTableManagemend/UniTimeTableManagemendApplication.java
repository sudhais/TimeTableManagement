package com.example.UniTimeTableManagemend;

import com.example.UniTimeTableManagemend.models.Course;
import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.models.TimeTable;
import com.example.UniTimeTableManagemend.models.enums.Day;
import com.example.UniTimeTableManagemend.models.enums.Faculty;
import com.example.UniTimeTableManagemend.models.enums.Location;
import com.example.UniTimeTableManagemend.respositories.RoomRepository;
import com.example.UniTimeTableManagemend.respositories.TimeTableRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

//	@Bean
//	CommandLineRunner runner(RoomRepository roomRepository){
//		return args -> {
//			Room room = new Room(Location.CLASS1,
//					LocalTime.of(12,00),
//					LocalTime.of(14,30),
//					Day.MONDAY,
//					"SE3020");
//			roomRepository.insert(room);
//		};
//	}

//	@Bean
//	CommandLineRunner runner(TimeTableRepository timeTableRepository){
//		return args -> {
//
//			List<String> code = new ArrayList<>();
//			List<Course> courses = new ArrayList<>();
//			List<Room> rooms = new ArrayList<>();
//			code.add("SE3030");
//			code.add("SE3020");
//			Room room = new Room(Location.CLASS1,
//					"11:30",
//					"12:30",
//					Day.MONDAY,
//					"SE3020");
//			Course course = new Course("SE3010","AF","what to do", 4, Faculty.IT);
//			courses.add(course);
//			rooms.add(room);
//			TimeTable timeTable = new TimeTable(code,courses,rooms);
//			timeTableRepository.insert(timeTable);
//	};}

}
