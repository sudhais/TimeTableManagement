package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.RoomException;
import com.example.UniTimeTableManagemend.exception.TimeTableException;
import com.example.UniTimeTableManagemend.models.Course;
import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.models.Table;
import com.example.UniTimeTableManagemend.models.TimeTable;
import com.example.UniTimeTableManagemend.models.enums.Day;
import com.example.UniTimeTableManagemend.models.enums.Location;
import com.example.UniTimeTableManagemend.respositories.TimeTableRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
class TimeTableServiceTest {

    @Mock
    private TimeTableRepository timeTableRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private TimeTableService timeTableService;


    @Test
    void getTimeTable_success() throws Exception {
        List<String> courseCodes = List.of("C001", "C002", "C003");
        List<Room> roomsC001 = new ArrayList<>();
        roomsC001.add(new Room(Location.CLASS2, "08:00", "10:00", Day.MONDAY, "C001"));
        roomsC001.add(new Room(Location.CLASS2, "10:00", "12:00", Day.TUESDAY, "C001"));
        when(roomService.findByCode("C001")).thenReturn(roomsC001);

        List<Room> roomsC002 = new ArrayList<>();
        roomsC002.add(new Room(Location.CLASS1, "08:00", "10:00", Day.WEDNESDAY, "C002"));
        when(roomService.findByCode("C002")).thenReturn(roomsC002);

        List<Room> roomsC003 = new ArrayList<>();
        roomsC003.add(new Room(Location.CLASS1, "10:00", "12:00", Day.THURSDAY, "C003"));
        when(roomService.findByCode("C003")).thenReturn(roomsC003);

        when(courseService.findByCourseCode("C001")).thenReturn(new Course("C001", "Course 1", "Description 1", 3, null));
        when(courseService.findByCourseCode("C002")).thenReturn(new Course("C002", "Course 2", "Description 2", 3, null));
        when(courseService.findByCourseCode("C003")).thenReturn(new Course("C003", "Course 3", "Description 3", 3, null));

        timeTableService.addTimeTable(courseCodes);

        verify(timeTableRepository, times(1)).insert(any(TimeTable.class));
    }

    @Test
    void addTimeTable_success() throws Exception {
        List<String> courseCodes = List.of("C001", "C002", "C003");
        when(timeTableRepository.findTimeTableByCourseCodesContainsAll(courseCodes)).thenReturn(new ArrayList<>());
        when(courseService.findByCourseCode(anyString())).thenReturn(new Course());
        when(roomService.findByCode(anyString())).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> timeTableService.addTimeTable(courseCodes));

        verify(timeTableRepository, times(1)).insert(any(TimeTable.class));
    }

    @Test
    void testAddTimeTable_TimeTableExists() {
        List<String> courseCodes = List.of("C001", "C002", "C003");
        List<TimeTable> existingTimeTables = new ArrayList<>();
        List<Table> tables = new ArrayList<>();
        tables.add(new Table());
        existingTimeTables.add(new TimeTable(courseCodes,tables));
        when(timeTableRepository.findTimeTableByCourseCodesContainsAll(courseCodes)).thenReturn(existingTimeTables);

        TimeTableException exception = assertThrows(TimeTableException.class, () -> timeTableService.addTimeTable(courseCodes));
        assertEquals("Time Table Already exists", exception.getMessage());

        verify(timeTableRepository, never()).insert(any(TimeTable.class));
    }

    @Test
    void testAddTimeTable_ExceptionThrown() throws CourseException {
        List<String> courseCodes = List.of("C001", "C002", "C003");
        when(timeTableRepository.findTimeTableByCourseCodesContainsAll(courseCodes)).thenReturn(new ArrayList<>());
        when(courseService.findByCourseCode(anyString())).thenThrow(new CourseException("Course not found"));

        CourseException exception = assertThrows(CourseException.class, () -> timeTableService.addTimeTable(courseCodes));
        assertEquals("Course not found", exception.getMessage());

        verify(timeTableRepository, never()).insert(any(TimeTable.class));
    }

    @Test
    void testDeleteTimeTable_TimeTableExists() {
        String timeTableId = "timeTableId";
        when(timeTableRepository.findById(timeTableId)).thenReturn(Optional.of(new TimeTable()));

        assertDoesNotThrow(() -> timeTableService.deleteTimeTable(timeTableId));

        verify(timeTableRepository, times(1)).deleteById(timeTableId);
    }

    @Test
    void testDeleteTimeTable_TimeTableDoesNotExist() {
        String timeTableId = "nonExistentTimeTableId";
        when(timeTableRepository.findById(timeTableId)).thenReturn(Optional.empty());

        TimeTableException exception = assertThrows(TimeTableException.class, () -> timeTableService.deleteTimeTable(timeTableId));
        assertEquals("Time Table id: nonExistentTimeTableId does not exists", exception.getMessage());

        verify(timeTableRepository, never()).deleteById(anyString());
    }

    @Test
    void testAddClassSession_Success() throws CourseException, RoomException, ConstraintViolationException {
        Room room = new Room(Location.CLASS1,"10:00","11:00",Day.MONDAY,"C001");
        when(courseService.findByCourseCode("C001")).thenReturn(new Course("C001","DS","sdfsf",4,null));
        when(roomService.availability(room,false)).thenReturn(true);

        assertDoesNotThrow(() -> timeTableService.addClassSession(room));

        verify(roomService, times(1)).insertRoom(room);
    }

}