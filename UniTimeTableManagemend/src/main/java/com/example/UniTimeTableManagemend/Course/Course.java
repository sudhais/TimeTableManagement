package com.example.UniTimeTableManagemend.Course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Course {

    @Id
    private String id;
    @NotNull(message = "course code cannot be null")
    @NotBlank(message = "course code cannot be blank")
    @Indexed(unique = true)
    private String code;
    @NotNull(message = "course name cannot be null")
    @NotBlank(message = "course name cannot be blank")
    private String name;
    @NotNull(message = "course description cannot be null")
    @NotBlank(message = "course description cannot be blank")
    private String description;
    private int credit;

    public Course(String code, String name, String description, int credit) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.credit = credit;
    }
}
