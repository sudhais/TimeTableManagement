package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.RoomException;
import com.example.UniTimeTableManagemend.exception.TimeTableException;
import com.example.UniTimeTableManagemend.models.Course;
import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.models.Table;
import com.example.UniTimeTableManagemend.models.TimeTable;
import com.example.UniTimeTableManagemend.respositories.TimeTableRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TimeTableService {

    private final TimeTableRepository timeTableRepository;
    private CourseService courseService;
    private RoomService roomService;

    public TimeTable getTimeTable(List<String> courseCodes) throws TimeTableException, CourseException {
        //find and get given by course codes
        List<TimeTable> timeTableList= timeTableRepository.findTimeTableByCourseCodesContainsAll(courseCodes);
        //check timetable list empty or not
        if(!timeTableList.isEmpty()){
            //iterate the timetable list
            for(TimeTable timeTable: timeTableList){
                //check no of course codes are same
                if(timeTable.getCourseCodes().size() == courseCodes.size()){
                    return timeTable;
                }
            }
        }
        //call function to add
        addTimeTable(courseCodes);
        return getTimeTable(courseCodes);
    }

    public void addTimeTable(List<String> courseCodes) throws CourseException, TimeTableException {

        //find and get given by course codes
        List<TimeTable> timeTableList= timeTableRepository.findTimeTableByCourseCodesContainsAll(courseCodes);
        //check if it is already exists or not
        if(!timeTableList.isEmpty()){
            System.out.println(timeTableList);
            //iterate timetable list
            for(TimeTable timeTable: timeTableList){
                //check no of course code are same
                if(timeTable.getCourseCodes().size() == courseCodes.size()){
                    throw new TimeTableException(TimeTableException.AlreadyExists());
                }
            }
        }

        List<Table> tables = new ArrayList<>();
        Course course ;

        //iterate the course codes
        for (String code : courseCodes ){
            List<Room> rooms = roomService.findByCode(code);
            course = courseService.findByCourseCode(code);
            if(rooms.size()>0){
                for (Room room : rooms){
                    tables.add(new Table(
                            room.getDay(),
                            room.getStartTime(),
                            room.getEndTime(),
                            room.getLocation(),
                            room.getCourseCode(),
                            course.getName()
                    ));
                }
                rooms.clear();
            }

        }
        timeTableRepository.insert(new TimeTable(courseCodes, tables));
    }

    public void updateTimeTable(String id,List<String> courseCodes) throws TimeTableException {

        TimeTable timeTable = timeTableRepository.findById(id)
                .orElseThrow(()->new TimeTableException(TimeTableException.NotFoundException(id)));

        //find and get given by course codes
        List<TimeTable> timeTableList= timeTableRepository.findTimeTableByCourseCodesContainsAll(courseCodes);
        //check timetable list empty or not
        if(!timeTableList.isEmpty()){
            //iterate the timetable list
            for(TimeTable timeTable1: timeTableList){
                //check no of course codes are same
                if(timeTable.getCourseCodes().size() == courseCodes.size()){
                    deleteTimeTable(id);
                }
            }
        }
    }

    public void deleteTimeTable(String id) throws TimeTableException {
        timeTableRepository.findById(id)
                .orElseThrow(()->new TimeTableException(TimeTableException.NotFoundException(id)));

        timeTableRepository.deleteById(id);
    }

    public void addClassSession(Room room) throws CourseException, RoomException, ConstraintViolationException, TimeTableException {

        roomService.insertRoom(room);
        timeTableChange(room.getCourseCode());
    }

    public Room updateClassSession(List<Room> roomList) throws RoomException, ConstraintViolationException, CourseException {
        Room room1 = roomService.getRoomData(roomList.get(0));
        roomService.updateRoom(room1.getId(),roomList.get(1));
        roomList.get(1).setId(room1.getId());

        if(!roomList.get(0).getCourseCode().equals(roomList.get(1).getCourseCode())){
            timeTableChange(roomList.get(0).getCourseCode());
            timeTableChange(roomList.get(1).getCourseCode());
        }else
            timeTableChange(roomList.get(0).getCourseCode());

        return roomList.get(1);
    }

    public void deleteClassSession(Room room) throws RoomException, TimeTableException, CourseException {
        Room room1 = roomService.getRoomData(room);
        roomService.deleteRoom(room1.getId());
        timeTableChange(room.getCourseCode());
    }

    public void timeTableChange(String courseCode){

        List<TimeTable> timeTableList = getAllTimeTable(courseCode);
        if(timeTableList == null)
            return;

        for(TimeTable timeTable: timeTableList){
            timeTableRepository.delete(timeTable);
        }
    }

    //get all timetable data given by code
    public List<TimeTable> getAllTimeTable(String courseCodes){
        //find and get given by course codes
        List<TimeTable> timeTableList= timeTableRepository.findTimeTableByCourseCode(courseCodes);
        //check timetable list empty or not
        if(!timeTableList.isEmpty()){
            return timeTableList;
        }else
            return null;
    }
}
