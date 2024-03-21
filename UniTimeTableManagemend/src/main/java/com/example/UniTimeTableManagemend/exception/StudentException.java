package com.example.UniTimeTableManagemend.exception;

public class StudentException extends Exception{

    public StudentException(String message){
        super(message);
    }

    public static String NotFoundException(String id) {
        return "Student id: " + id + " does not exists";
    }

    public static String AlreadyExists(String name) {
        return "Student name: " + name + " Already exists";
    }

}
