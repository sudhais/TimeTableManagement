package com.example.UniTimeTableManagemend.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@NoArgsConstructor
public class TimeTable {

    @Id
    private String id;
    private List<String> courseCodes;

    private List<Table> tables;

    public TimeTable(List<String> courseCodes, List<Table> tables) {
        this.courseCodes = courseCodes;
        this.tables = tables;
    }
}
