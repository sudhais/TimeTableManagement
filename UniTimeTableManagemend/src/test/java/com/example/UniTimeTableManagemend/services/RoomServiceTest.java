package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.RoomException;
import com.example.UniTimeTableManagemend.models.Course;
import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.models.enums.Day;
import com.example.UniTimeTableManagemend.models.enums.Faculty;
import com.example.UniTimeTableManagemend.models.enums.Location;
import com.example.UniTimeTableManagemend.respositories.RoomRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private RoomService roomService;


    @Test
    void getAllRooms_success() {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(Location.CLASS1, "08:00", "10:00", Day.MONDAY, "C001"));
        rooms.add(new Room(Location.CLASS2, "10:00", "12:00", Day.TUESDAY, "C002"));

        when(roomRepository.findAll()).thenReturn(rooms);

        List<Room> result = roomService.getAllRooms();

        assertEquals(2, result.size());
        assertEquals(rooms, result);
    }

    @Test
    void getAllRooms_noDataInRepository_emptyListReturned() {
        when(roomRepository.findAll()).thenReturn(new ArrayList<>());

        List<Room> courses = roomService.getAllRooms();

        assertNotNull(courses);
        assertTrue(courses.isEmpty());
    }

    @Test
    void insertRoom_success() throws CourseException, RoomException,ConstraintViolationException {

        Room room = new Room(Location.CLASS1,"08:10","10:20",Day.MONDAY,"SE3020");

        when(courseService.findByCourseCode("SE3020")).thenReturn(new Course("CS001","SS","sdf",3,null));
        when(roomRepository.findRoomByDayAndLocationOrderByStartTime(any(),any())).thenReturn(Optional.empty());

        roomService.insertRoom(room);

        verify(roomRepository,times(1)).insert(room);
        verify(notificationService,times(1)).addNotification(anyString());
    }

    @Test
    void insertRoom_overlapping() throws CourseException, RoomException, ConstraintViolationException {
        Room room = new Room(Location.CLASS1, "08:00", "10:00", Day.MONDAY, "SE3020");

        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(Location.CLASS1, "08:00", "10:00", Day.MONDAY, "C001"));
        rooms.add(new Room(Location.CLASS1, "10:00", "12:00", Day.MONDAY, "C002"));

        // Mocking the behavior of CourseService to throw CourseException when findByCourseCode is called
        when(courseService.findByCourseCode("SE3020")).thenReturn(new Course("CS001","SS","sdf",3,null));
        when(roomRepository.findRoomByDayAndLocationOrderByStartTime(any(),any())).thenReturn(Optional.of(rooms));

        assertThrows(RoomException.class, () -> roomService.insertRoom(room));

        // Verifying that addNotification is not called
        verify(notificationService, never()).addNotification(anyString());


        // Verifying that insert method of RoomRepository is not called
        verify(roomRepository, never()).insert(room);
    }

    @Test
    void insertRoom_CourseNotFound() throws CourseException, RoomException, ConstraintViolationException {
        Room room = new Room(Location.CLASS1, "08:00", "10:00", Day.MONDAY, "SE3020");

        // Mocking the behavior of CourseService to throw CourseException when findByCourseCode is called
        when(courseService.findByCourseCode("SE3020")).thenThrow(new CourseException("not found"));


        // Verifying that insert method of RoomRepository is not called
        assertThrows(CourseException.class, () -> roomService.insertRoom(room));

        // Verifying that addNotification method of NotificationService is not called
        verify(notificationService, never()).addNotification(anyString());

        // Verifying that insert method of RoomRepository is not called
        verify(roomRepository, never()).insert(room);
    }

    @Test
    void deleteRoom_success() throws RoomException {

        when(roomRepository.existsById(anyString())).thenReturn(true);

        roomService.deleteRoom("1");

        verify(roomRepository,times(1)).deleteById("1");

    }

    @Test
    void deleteRoom_roomIdNotExists_exceptionThrown() {

        when(roomRepository.existsById(anyString())).thenReturn(false);

        assertThrows(RoomException.class , () -> roomService.deleteRoom("1"));

        verify(roomRepository, never()).deleteById("1");

    }

    @Test
    void updateRoom_success() throws CourseException, RoomException {
        Room existingRoom = new Room(Location.CLASS2, "08:00", "10:00", Day.MONDAY, "C001");
        Room updatedRoom = new Room(Location.CLASS1, "10:00", "12:00", Day.TUESDAY, "C002");

        when(roomRepository.findById("roomId")).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(any(Room.class))).thenReturn(updatedRoom);
        when(courseService.findByCourseCode(updatedRoom.getCourseCode())).thenReturn(new Course("C002", "Course 2", "Description", 3, null));

        roomService.updateRoom("roomId", updatedRoom);

        verify(roomRepository, times(1)).save(updatedRoom);
        verify(notificationService, times(1)).addNotification(anyString());
    }

    @Test
    void updateRoom_OverlappingTimeSlots_exceptionThrown() throws CourseException {
        Room existingRoom = new Room("123",Location.CLASS2, "08:00", "10:00", Day.MONDAY, "C001");
        Room updatedRoom = new Room("123",Location.CLASS1, "10:00", "12:00", Day.TUESDAY, "C002");

        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room("222",Location.CLASS1, "08:00", "10:00", Day.MONDAY, "C001"));
        rooms.add(new Room("234",Location.CLASS1, "10:00", "12:00", Day.MONDAY, "C002"));

        when(roomRepository.findById("123")).thenReturn(Optional.of(existingRoom));
        when(courseService.findByCourseCode(updatedRoom.getCourseCode())).thenReturn(new Course("C002", "Course 2", "Description", 3, null));
        when(roomRepository.findRoomByDayAndLocationOrderByStartTime(any(),any())).thenReturn(Optional.of(rooms));

        assertThrows(RoomException.class, () -> roomService.updateRoom("123", updatedRoom));

        verify(roomRepository, never()).save(updatedRoom);
        verify(notificationService, never()).addNotification(anyString());
    }

    @Test
    void testUpdateRoom_NonExistentCourse() throws CourseException {
        Room existingRoom = new Room(Location.CLASS1, "08:00", "10:00", Day.MONDAY, "C001");
        Room updatedRoom = new Room(Location.CLASS2, "10:00", "12:00", Day.TUESDAY, "NonExistentCourse");

        when(roomRepository.findById("roomId")).thenReturn(Optional.of(existingRoom));
        when(courseService.findByCourseCode(updatedRoom.getCourseCode())).thenThrow(new CourseException("Course not found"));

        assertThrows(CourseException.class, () -> roomService.updateRoom("roomId", updatedRoom));

        verify(roomRepository, never()).save(updatedRoom);
        verify(notificationService, never()).addNotification(anyString());
    }

    @Test
    void testUpdateRoom_NonExistentRoom() {
        Room updatedRoom = new Room(Location.CLASS2, "10:00", "12:00", Day.TUESDAY, "NonExistentCourse");
        when(roomRepository.findById("NonExistentRoomId")).thenReturn(Optional.empty());

        assertThrows(RoomException.class, () -> roomService.updateRoom("NonExistentRoomId", updatedRoom));

        verify(roomRepository, never()).save(updatedRoom);
        verify(notificationService, never()).addNotification(anyString());
    }
}