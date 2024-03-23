package com.example.UniTimeTableManagemend.respositories;

import com.example.UniTimeTableManagemend.models.TimeTable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TimeTableRepository extends MongoRepository<TimeTable,String> {

    @Query("{courseCodes:{$all:?0}}")
    List<TimeTable> findTimeTableByCourseCodesContainsAll(List<String> courseCodes);
    Optional<TimeTable> findTimeTableByCourseCodes(List<String> courseCodes);
}
