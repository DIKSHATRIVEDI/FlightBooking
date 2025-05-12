package com.example.flight_and_search_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    //public Long id;
    @NotBlank(message = "Flight number is required")
    @Column(nullable = false, unique = true)
    public String flightNumber;

    @NotBlank(message = "Airline name is required")
    public String airline;

    @NotBlank(message = "Source is required")
    public String source;

    @NotBlank(message = "Destination is required")
    public String destination;

    @NotNull(message = "Departure date is required")
    @FutureOrPresent(message = "Departure date cannot be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate departureDate;

    @NotBlank(message = "Departure time is required")
    public String departureTime;

    @NotBlank(message = "Arrival time is required")
    public String arrivalTime;

    @Min(value = 1, message = "At least one seat must be available")
    public int availableSeats;

    @Positive(message = "Fare must be a positive value")
    public double fare;
}
