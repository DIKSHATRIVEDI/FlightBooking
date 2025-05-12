package com.example.booking_service.dto;

import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@NoArgsConstructor
public class FlightResponseDTO {
    public Long id;
    public String flightNumber;
    public String airline;
    public String source;
    public String destination;
    public LocalDate departureDate;
    public String departureTime;
    public String arrivalTime;
    public int availableSeats;
    public double fare;
    public List<PassengerDTO> passengers;

    public FlightResponseDTO(Long id, String flightNumber, String airline, String source, String destination,
                             LocalDate departureDate, String departureTime, String arrivalTime, int availableSeats,
                             double fare, List<PassengerDTO> passengers) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.source = source;
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
        this.fare = fare;
        this.passengers = passengers;
    }
}


