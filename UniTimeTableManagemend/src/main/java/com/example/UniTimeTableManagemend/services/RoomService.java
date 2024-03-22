package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.RoomException;
import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.respositories.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private CourseService courseService;

    public List<Room> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        if(rooms.size() > 0 )
            return rooms;
        else
            return new ArrayList<Room>();
    }

    public void insertRoom(Room room) throws CourseException, RoomException {

        //converting LocalTime to String
        room.setStartTime(room.getStartTime().formatted(DateTimeFormatter.ofPattern("HH:mm")));
        room.setEndTime(room.getEndTime().formatted(DateTimeFormatter.ofPattern("HH:mm")));

        //check course code exists or not
        courseService.findByCourseCode(room.getCourseCode());
        roomRepository.insert(room);

    }

    public Boolean availability(Room room){

        //get all list of room details by given date and location
       Optional<List<Room>> rooms = roomRepository.findRoomByDayAndLocation(room.getDay(),room.getLocation());

       if(rooms.isPresent()){

           for(Room room1 : rooms.get()){

           }

       }
        return false;

//        rooms.forEach(room -> {
//            room.setStartTime(LocalTime.parse(room.getStartTime(),DateTimeFormatter.ofPattern("HH:mm")));
//        });
    }


}
