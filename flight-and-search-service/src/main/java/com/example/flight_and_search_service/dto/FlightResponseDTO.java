package com.example.flight_and_search_service.dto;

import com.example.flight_and_search_service.model.Flight;

import java.time.LocalDate;

public class FlightResponseDTO {
    //public Long id;
    public String flightNumber;
    public String airline;
    public String source;
    public String destination;
    public LocalDate departureDate;
    public String departureTime;
    public String arrivalTime;
    public int availableSeats;
    public double fare;

    public FlightResponseDTO(Flight flight) {
        //this.id = flight.getId();
        this.flightNumber = flight.getFlightNumber();
        this.airline = flight.getAirline();
        this.source = flight.getSource();
        this.destination = flight.getDestination();
        this.departureDate = flight.getDepartureDate();
        this.departureTime = flight.getDepartureTime();
        this.arrivalTime = flight.getArrivalTime();
        this.availableSeats = flight.getAvailableSeats();
        this.fare = flight.getFare();
    }
}

