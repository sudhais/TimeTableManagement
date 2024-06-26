package com.example.UniTimeTableManagemend.respositories;

import com.example.UniTimeTableManagemend.models.TimeTable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TimeTableRepository extends MongoRepository<TimeTable,String> {

    @Query("{courseCodes:{$all:?0}}")
    List<TimeTable> findTimeTableByCourseCodesContainsAll(List<String> courseCodes);
    @Query("{courseCodes:{$eq:?0}}")
    List<TimeTable> findTimeTableByCourseCode(String courseCodes);
}
