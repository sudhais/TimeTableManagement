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

    @NotNull(message = "user name cannot be null")
    @NotBlank(message = "user name cannot be blank")
    private String name;
    private List<String> courseCodes;
    private String password;

    @NotNull(message = "user role cannot be null")
    @NotBlank(message = "user role cannot be blank")
    private Role role;

    public User(String name, List<String> courseCodes, String password, Role role) {
        this.name = name;
        this.courseCodes = courseCodes;
        this.password = password;
        this.role = role;
    }
}
