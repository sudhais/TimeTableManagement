package com.example.UniTimeTableManagemend.exception;

import com.example.UniTimeTableManagemend.models.Room;

public class RoomException extends Exception{

    public RoomException(String message){
        super(message);
    }

    public static String Overlap(Room room){
        return "Time is overlapping with " + room;
    }

    public static String NotFoundException(String id) {
        return "Room id: " + id + " does not exists";
    }

    public static String NoContent(){
        return "Room is full time available";
    };

    public static String NullValues(String name){
        return name + " Cannot be blank";
    }

    public static String BlankValues(String name){
        return name + " Cannot be null";
    }
}
