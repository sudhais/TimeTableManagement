package com.example.UniTimeTableManagemend.controllers;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.RoomException;
import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.services.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/room")
@AllArgsConstructor
public class RoomController {

    private RoomService roomService;

    @GetMapping
    public ResponseEntity<?> getAllRooms(){
        List<Room> rooms = roomService.getAllRooms();
        return new ResponseEntity<>(rooms, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> insertRoom(@RequestBody Room room){

        try {
            roomService.insertRoom(room);
            return new ResponseEntity<>("inserted " + room, HttpStatus.OK);
        }catch (CourseException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }catch (RoomException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }
}
