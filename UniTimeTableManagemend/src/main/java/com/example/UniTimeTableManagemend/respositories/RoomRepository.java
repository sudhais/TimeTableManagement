package com.example.UniTimeTableManagemend.respositories;

import com.example.UniTimeTableManagemend.models.Room;
import com.example.UniTimeTableManagemend.models.enums.Day;
import com.example.UniTimeTableManagemend.models.enums.Location;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends MongoRepository<Room,String> {


    Optional<List<Room>> findRoomByDayAndLocationOrderByStartTime(Day day, Location location);
    Optional<List<Room>> findRoomByCourseCode(String courseCode);
}
