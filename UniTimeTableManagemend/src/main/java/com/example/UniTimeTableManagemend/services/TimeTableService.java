package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.RoomException;
import com.example.UniTimeTableManagemend.exception.TimeTableException;
import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.models.TimeTable;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

public interface TimeTableService {
    public TimeTable getTimeTable(List<String> courseCodes) throws TimeTableException, CourseException;
    public void addTimeTable(List<String> courseCodes) throws CourseException, TimeTableException;
    public void deleteTimeTable(String id) throws TimeTableException;
    public void addClassSession(Room room) throws CourseException, RoomException, ConstraintViolationException, TimeTableException;
    public Room updateClassSession(List<Room> roomList) throws RoomException, ConstraintViolationException, CourseException;
    public void deleteClassSession(Room room) throws RoomException, TimeTableException, CourseException;
}
