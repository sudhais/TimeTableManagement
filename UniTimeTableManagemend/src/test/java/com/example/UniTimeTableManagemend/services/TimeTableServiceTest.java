package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.TimeTableException;
import com.example.UniTimeTableManagemend.models.TimeTable;
import com.example.UniTimeTableManagemend.respositories.TimeTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class TimeTableServiceTest {

    @Mock
    private TimeTableRepository timeTableRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private TimeTableService timeTableService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getTimeTable_TimeTableFound() throws Exception {
        // Test scenario setup
        List<String> courseCodes = new ArrayList<>();
        courseCodes.add("C001");
        // Mocking the repository behavior
        List<TimeTable> timeTableList = new ArrayList<>();
        TimeTable mockTimeTable = new TimeTable(courseCodes, new ArrayList<>());
        timeTableList.add(mockTimeTable);
        when(timeTableRepository.findTimeTableByCourseCodesContainsAll(courseCodes)).thenReturn(timeTableList);

        // Calling the method under test
        TimeTable result = timeTableService.getTimeTable(courseCodes);

        // Verifying the behavior
        assertEquals(mockTimeTable, result);
    }

    @Test
    void getTimeTable_TimeTableNotFound() throws Exception {
        // Test scenario setup
        List<String> courseCodes = new ArrayList<>();
        courseCodes.add("C001");
        when(timeTableRepository.findTimeTableByCourseCodesContainsAll(courseCodes)).thenReturn(new ArrayList<>());

        // Calling the method under test
        assertThrows(TimeTableException.class, () -> timeTableService.getTimeTable(courseCodes));
    }

}