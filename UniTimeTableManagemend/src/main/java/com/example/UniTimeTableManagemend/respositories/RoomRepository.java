package com.example.UniTimeTableManagemend.respositories;

import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.models.enums.Day;
import com.example.UniTimeTableManagemend.models.enums.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RoomRepository extends MongoRepository<Room,String> {


    Optional<List<Room>> findRoomByDayAndLocationOrderByStartTime(Day day, Location location);
    Optional<List<Room>> findRoomByCourseCode(String courseCode);

    Optional<Room> findRoomByDayAndLocationAndCourseCodeAndStartTime(Day day, Location location, String courseCode, String startTime);
}
