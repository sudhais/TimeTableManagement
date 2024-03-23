package com.example.UniTimeTableManagemend.exception;

public class TimeTableException extends Exception{

    public TimeTableException(String message){
        super(message);
    }

    public static String NotFoundException(String id) {
        return "Time Table id: " + id + " does not exists";
    }

    public static String AlreadyExists() {
        return "Time Table Already exists";
    }
}
