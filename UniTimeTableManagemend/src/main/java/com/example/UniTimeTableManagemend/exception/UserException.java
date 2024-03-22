package com.example.UniTimeTableManagemend.exception;

public class UserException extends Exception{

    public UserException(String message){
        super(message);
    }

    public static String NotFoundException(String id) {
        return "User id: " + id + " does not exists";
    }

    public static String AlreadyExists(String name) {
        return "User name: " + name + " Already exists";
    }

}
