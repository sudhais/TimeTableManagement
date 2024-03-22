package com.example.UniTimeTableManagemend.exception;

public class RoomException extends Exception{

    public RoomException(String message){
        super(message);
    }

    public static String timeLimit(){
        return "Time should be between 00.00 - 24.00";
    }
}
