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
    private NotificationService notificationService;

    public List<Room> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        if(rooms.size() > 0 )
            return rooms;
        else
            return new ArrayList<>();
    }

    public void insertRoom(Room room) throws CourseException, RoomException, ConstraintViolationException {
        //validate
        roomValidate(room);

        //check course code exists or not
        courseService.findByCourseCode(room.getCourseCode());

        //check the condition and insert
        if(availability(room, false)){
            //insert to mongodb
            roomRepository.insert(room);
            System.out.println("Inserted " + room);
            notificationService.addNotification("class session added check the timetable");
        }

    }

    public void deleteRoom(String roomId) throws RoomException {
        //check roomId in mongodb exists or not
        if(roomRepository.existsById(roomId)){
            //delete room data in mongodb
            roomRepository.deleteById(roomId);
            System.out.println("Deleted " + roomId);
            notificationService.addNotification("class session removed check the timetable");
        }else
            throw new RoomException(RoomException.NotFoundException(roomId));

    }

    public void updateRoom(String roomid, Room room) throws RoomException, CourseException {
        //validate new room object
        roomValidate(room);

        //check room id exists or not if not throw that there were no data with roomid
        Room room1 = roomRepository.findById(roomid)
                .orElseThrow(()-> new RuntimeException(RoomException.NotFoundException(roomid)));

        //check same course code or not
        if(!room1.getCourseCode().equals(room.getCourseCode())){
            // check course code exists or not
            courseService.findByCourseCode(room.getCourseCode());
        }

        //set data from updated data
        room1.setCourseCode(room.getCourseCode());
        room1.setStartTime(room.getStartTime());
        room1.setEndTime(room.getEndTime());
        room1.setDay(room.getDay());
        room1.setLocation(room.getLocation());

        //check the condition and insert
        if(availability(room1,true)){
            //update to the mongodb
            roomRepository.save(room1);
            System.out.println("Updated " + room1);
            notificationService.addNotification("class session changed check the time table");
        }


    }

    //check new room is available or not
    public Boolean availability(Room room, Boolean update) throws RoomException{

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

               //check this is update or insert
               if(update){
                   //if room are same except course code and if it is then ignore this time check
                   if(room1.getId().equals(room.getId()) &&
                           room1.getStartTime().equals(room.getStartTime()) &&
                           room1.getEndTime().equals(room.getEndTime()) &&
                           room1.getDay().equals(room.getDay()) &&
                           room1.getLocation().equals(room.getLocation()))
                       continue;
               }

               //converting string to local time and initialize to the variable
               start = LocalTime.parse(room1.getStartTime(),DateTimeFormatter.ofPattern("HH:mm"));
               end = LocalTime.parse(room1.getEndTime(),DateTimeFormatter.ofPattern("HH:mm"));

               //check whether overlapping or not
               if((startTime.isAfter(start) && startTime.isBefore(end))|| (endTime.isAfter(start) && endTime.isBefore(end)) ||
                       (room.getStartTime().equals(room1.getStartTime()) && room.getEndTime().equals(room1.getEndTime()))) {
                   System.out.println("overlapping with" + room1);
                   throw new RoomException(RoomException.Overlap(room1));
               }
           }

       }
        return true;

    }

    //get room data given by day, location, courseCode , start time
    public Room getRoomData(Room room) throws RoomException {
        return roomRepository.findRoomByDayAndLocationAndCourseCodeAndStartTime(
                room.getDay(),room.getLocation(),room.getCourseCode(),room.getStartTime())
                .orElseThrow(()->new RoomException(RoomException.NotFoundException("....")));
    }

    //get all rooms given by day and location and sort by start time
    public List<Room> locationTimes(Day day, Location location) throws RoomException{
        Optional<List<Room>> rooms = roomRepository.findRoomByDayAndLocationOrderByStartTime(day,location);

        if(rooms.isPresent()){
            return rooms.get();
        }else
            throw new RoomException(RoomException.NoContent());


    }

    public List<Room> findByCode(String courseCode) throws CourseException{
        return roomRepository.findRoomByCourseCode(courseCode)
                 .orElseThrow(()-> new CourseException(CourseException.NotFoundException("Course Code", courseCode)));
    }

    //validate room properties method
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
