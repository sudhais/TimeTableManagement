package com.example.UniTimeTableManagemend.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class User {

    @Id
    private String id;

    @NotNull(message = "student name cannot be null")
    @NotBlank(message = "student name cannot be blank")
    private String name;
    private List<String> courseCodes;
    private String password;

    private Role role;

    public User(String name, List<String> courseCodes, String password, Role role) {
        this.name = name;
        this.courseCodes = courseCodes;
        this.password = password;
        this.role = role;
    }
}
