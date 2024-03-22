package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.RoomException;
import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.models.enums.Day;
import com.example.UniTimeTableManagemend.models.enums.Location;
import com.example.UniTimeTableManagemend.respositories.RoomRepository;
import jakarta.validation.ConstraintViolationException;
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

    public void insertRoom(Room room) throws CourseException, RoomException, ConstraintViolationException {
        //validate
        roomValidate(room);

        //check course code exists or not
        courseService.findByCourseCode(room.getCourseCode());

        //check the condition and insert
        if(availability(room)){
            roomRepository.insert(room);
            System.out.println("Inserted " + room);
        }



    }

    public Boolean availability(Room room) throws RoomException{

        //converting string to local time and initialize to the variable
        LocalTime startTime,endTime;
        startTime = LocalTime.parse(room.getStartTime(),DateTimeFormatter.ofPattern("HH:mm"));
        endTime = LocalTime.parse(room.getEndTime(),DateTimeFormatter.ofPattern("HH:mm"));

        //get all list of room details by given date and location and sorted by startTime
       Optional<List<Room>> rooms = roomRepository.findRoomByDayAndLocationOrderByStartTime(room.getDay(),room.getLocation());

        //check rooms list exists or not
       if(rooms.isPresent()){

           LocalTime start,end;

           //iterate the rooms list
           for(Room room1 : rooms.get()){
               //converting string to local time and initialize to the variable
               start = LocalTime.parse(room1.getStartTime(),DateTimeFormatter.ofPattern("HH:mm"));
               end = LocalTime.parse(room1.getEndTime(),DateTimeFormatter.ofPattern("HH:mm"));

               //check whether overlapping or not
               if((startTime.isAfter(start) && startTime.isBefore(end))|| (endTime.isAfter(start) && endTime.isBefore(end)) ) {
                   System.out.println("overlapping with" + room1);
                   throw new RoomException(RoomException.Overlap(room1));
               }
           }

       }
        return true;

    }

    public List<Room> locationTimes(Day day, Location location) throws RoomException{
        Optional<List<Room>> rooms = roomRepository.findRoomByDayAndLocationOrderByStartTime(day,location);

        if(rooms.isPresent()){
            return rooms.get();
        }else
            throw new RoomException(RoomException.NoContent());


    }

    private static void roomValidate(Room room) throws RoomException {

        if(room.getCourseCode() == null)
            throw new RoomException(RoomException.BlankValues("Course Code"));
        else{
            if(room.getCourseCode().isBlank())
                throw new RoomException(RoomException.NullValues("Course Code"));
        }

        if(room.getStartTime() == null)
            throw new RoomException(RoomException.BlankValues("Start Time"));
        else{
            if(room.getStartTime().isBlank())
                throw new RoomException(RoomException.NullValues("Start Time"));
        }

        if(room.getEndTime() == null)
            throw new RoomException(RoomException.BlankValues("End Time"));
        else{
            if(room.getEndTime().isBlank())
                throw new RoomException(RoomException.NullValues("End Time"));
        }

    }


}
