package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.RoomException;
import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.models.enums.Day;
import com.example.UniTimeTableManagemend.models.enums.Location;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

public interface RoomService {
    public List<Room> getAllRooms();
    public void insertRoom(Room room) throws CourseException, RoomException, ConstraintViolationException;
    public void deleteRoom(String roomId) throws RoomException;
    public void updateRoom(String roomid, Room room) throws RoomException, CourseException;
    public Boolean availability(Room room, Boolean update) throws RoomException;
    public Room getRoomData(Room room) throws RoomException;
    public List<Room> locationTimes(Day day, Location location) throws RoomException;
    public List<Room> findByCode(String courseCode) throws CourseException;
}
