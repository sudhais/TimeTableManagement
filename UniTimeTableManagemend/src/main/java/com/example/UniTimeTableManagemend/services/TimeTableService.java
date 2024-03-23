package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.TimeTableException;
import com.example.UniTimeTableManagemend.models.Course;
import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.models.Table;
import com.example.UniTimeTableManagemend.models.TimeTable;
import com.example.UniTimeTableManagemend.respositories.TimeTableRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}
