package com.example.UniTimeTableManagemend.controllers;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.RoomException;
import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.models.enums.Day;
import com.example.UniTimeTableManagemend.models.enums.Location;
import com.example.UniTimeTableManagemend.services.Impl.RoomServiceImp;
import com.example.UniTimeTableManagemend.services.RoomService;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class RoomController {

    private RoomService roomService;

    @GetMapping("/adminAndFaculty/room")
    public ResponseEntity<?> getAllRooms(){
        List<Room> rooms = roomService.getAllRooms();
        return new ResponseEntity<>(rooms, HttpStatus.CREATED);
    }

    @GetMapping("/adminAndFaculty/room/filter")
    public ResponseEntity<?> getAllRoomsByLocation(@RequestParam String day , @RequestParam String location){
        try {
            List<Room> rooms = roomService.locationTimes(Day.valueOf(day),Location.valueOf(location));
            return new ResponseEntity<>(rooms, HttpStatus.OK);
        }catch (RoomException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/adminAndFaculty/room")
    public ResponseEntity<?> insertRoom(@RequestBody Room room){

        try {
            roomService.insertRoom(room);
            return new ResponseEntity<>(room, HttpStatus.CREATED);
        }catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (CourseException | RoomException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/adminAndFaculty/room/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable String roomId){
        try {
            roomService.deleteRoom(roomId);
            return new ResponseEntity<>("Deleted " + roomId , HttpStatus.OK);
        }catch (RoomException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/adminAndFaculty/room/{roomId}")
    public ResponseEntity<?> updateRoom(@PathVariable String roomId, @RequestBody Room room){
        try {
            roomService.updateRoom(roomId,room);
            return new ResponseEntity<>("updated " + roomId , HttpStatus.OK);
        }catch (RoomException | CourseException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }
}
