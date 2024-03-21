package com.example.UniTimeTableManagemend.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Student {

    @Id
    private String id;

    @NotNull(message = "student name cannot be null")
    @NotBlank(message = "student name cannot be blank")
    private String name;
    private List<String> courseCodes;
    private String password;

}
