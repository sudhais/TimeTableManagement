package com.example.UniTimeTableManagemend.Course;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Course {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;
    private String name;

    private String description;
    private int credit;

    public Course(String code, String name, String description, int credit) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.credit = credit;
    }
}
