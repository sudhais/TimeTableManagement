package com.example.UniTimeTableManagemend.respositories;

import com.example.UniTimeTableManagemend.models.TimeTable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TimeTableRepository extends MongoRepository<TimeTable,String> {
}
