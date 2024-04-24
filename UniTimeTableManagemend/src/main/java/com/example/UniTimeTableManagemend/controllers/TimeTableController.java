package com.example.UniTimeTableManagemend.controllers;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.RoomException;
import com.example.UniTimeTableManagemend.exception.TimeTableException;
import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.models.TimeTable;
import com.example.UniTimeTableManagemend.services.Impl.TimeTableServiceImp;
import com.example.UniTimeTableManagemend.services.TimeTableService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class TimeTableController {

    private final TimeTableService timeTableService;

    @GetMapping("/adminAndFaculty/timetable")
    public ResponseEntity<?> getTimeTable(@RequestBody List<String> courseCodes){
        try {
           TimeTable timeTable1 = timeTableService.getTimeTable(courseCodes);
            return new ResponseEntity<>(timeTable1, HttpStatus.OK);
        }catch (CourseException | TimeTableException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/adminAndFaculty/timetable")
    public ResponseEntity<?> addTimeTable(@RequestBody List<String> courseCodes) {

        try {
            timeTableService.addTimeTable(courseCodes);
            return new ResponseEntity<>("Successfully inserted", HttpStatus.CREATED);
        }catch (CourseException | TimeTableException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

//    @PutMapping("/adminAndFaculty/timetable/{id}")
//    public ResponseEntity<?> updateTimeTable(@PathVariable String id, @RequestBody List<String> courseCodes){
//        try {
//            timeTableService.updateTimeTable(id,courseCodes);
//            return new ResponseEntity<>("Updated " + id, HttpStatus.OK);
//        }catch (TimeTableException e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
//        }
//    }

    @DeleteMapping("/adminAndFaculty/timetable/{id}")
    public ResponseEntity<?> deleteTimetable(@PathVariable String id){
        try {
            timeTableService.deleteTimeTable(id);
            return new ResponseEntity<>("Successfully Deleted" + id, HttpStatus.OK);
        } catch (TimeTableException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/adminAndFaculty/timetable/room")
    public ResponseEntity<?> addClassSession(@RequestBody Room room){
        try {
            timeTableService.addClassSession(room);
            return new ResponseEntity<>("Successfully inserted" + room, HttpStatus.CREATED);
        }catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (CourseException | RoomException | TimeTableException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/adminAndFaculty/timetable/room")
    public ResponseEntity<?> updateClassSession(@RequestBody List<Room> roomList){

        try {
           Room room = timeTableService.updateClassSession(roomList);
            return new ResponseEntity<>("Successfully Updated" + room, HttpStatus.OK);
        }catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (CourseException | RoomException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/adminAndFaculty/timetable/room")
    public ResponseEntity<?> deleteClassSession(@RequestBody Room room){
        try {
            timeTableService.deleteClassSession(room);
            return new ResponseEntity<>("Successfully Deleted" + room, HttpStatus.OK);
        } catch (CourseException | RoomException | TimeTableException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        }
    }
}
