package com.example.UniTimeTableManagemend.exception;

public class UserException extends Exception{

    public UserException(String message){
        super(message);
    }

    public static String NotFoundException(String name,String id) {
        return name + ": " + id + " does not exists";
    }

    public static String NotEnrolled(String code){
        return "course code: " + code + " not enrolled";
    }

    public static String AlreadyExists(String name) {
        return "User name: " + name + " Already exists";
    }

    public static String AlreadyExistsCode(String name){
        return "Course Code: " + name + " Already Enrolled";
    }

}
