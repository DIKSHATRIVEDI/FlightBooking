package com.example.booking_service.exception;

public class PassengerSaveException extends RuntimeException{
    public PassengerSaveException(String message){
        super(message);
    }
}
