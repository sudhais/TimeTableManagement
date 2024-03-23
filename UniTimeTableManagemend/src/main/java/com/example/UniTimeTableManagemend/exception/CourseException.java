package com.example.UniTimeTableManagemend.exception;

public class CourseException extends Exception{

    public CourseException(String message) {
        super(message);
    }

    public static String NotFoundException(String name, String id) {
        return name + ": " + id + " does not exists";
    }

    public static String AlreadyExists(String code) {
        return "Course code: " + code + " Already exists";
    }


}
