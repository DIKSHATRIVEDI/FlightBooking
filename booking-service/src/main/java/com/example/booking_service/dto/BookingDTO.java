package com.example.booking_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class BookingDTO {
    @NotNull(message = "Flight ID cannot be null")
    public Long flightId;

    @NotNull(message = "User ID cannot be null")
    public Long userId;

    @NotEmpty(message = "Passenger list cannot be empty")
    public List<@NotNull PassengerDTO> passengers;
    //public Long passengerId;

}
