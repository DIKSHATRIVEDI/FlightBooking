package com.example.flight_and_search_service.service;

import com.example.flight_and_search_service.dto.FlightDTO;
import com.example.flight_and_search_service.model.Flight;
import com.example.flight_and_search_service.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    private FlightDTO flightDTO; // Declare FlightDTO at class level

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        flightDTO = new FlightDTO();
        flightDTO.flightNumber = "AI123";
        flightDTO.airline = "Air India";
        flightDTO.source = "Mumbai";
        flightDTO.destination = "Delhi";
        flightDTO.departureDate = LocalDate.of(2024, 12, 25);
        flightDTO.departureTime = "10:00 AM";
        flightDTO.arrivalTime = "12:00 PM";
        flightDTO.availableSeats = 100;
        flightDTO.fare = 5000.0;
    }

    @Test
    void createFlight() {
        Flight savedFlight = new Flight();
        savedFlight.setFlightNumber("AI123");
        savedFlight.setAirline("Air India");
        savedFlight.setSource("Mumbai");
        savedFlight.setDestination("Delhi");
        savedFlight.setDepartureDate(LocalDate.of(2024, 12, 25));
        savedFlight.setDepartureTime("10:00 AM");
        savedFlight.setArrivalTime("12:00 PM");
        savedFlight.setAvailableSeats(100);
        savedFlight.setFare(5000.0);
        savedFlight.setId(1L); // Set the ID

        when(flightRepository.save(any(Flight.class))).thenReturn(savedFlight);

        // Act
        Flight result = flightService.createFlight(flightDTO);

        // Assert
        assertNotNull(result);
        assertEquals("AI123", result.getFlightNumber());
        assertEquals("Air India", result.getAirline());
        assertEquals("Mumbai", result.getSource());
        assertEquals("Delhi", result.getDestination());
        assertEquals(LocalDate.of(2024, 12, 25), result.getDepartureDate());
        assertEquals("10:00 AM", result.getDepartureTime());
        assertEquals("12:00 PM", result.getArrivalTime());
        assertEquals(100, result.getAvailableSeats());
        assertEquals(5000.0, result.getFare());
        assertEquals(1L, result.getId()); // Verify the ID is set

        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    @Test
    void getById() {
        // Arrange
        Long flightId = 1L;
        Flight flight = new Flight();
        flight.setFlightNumber("AI123");
        flight.setAirline("Air India");
        flight.setSource("Mumbai");
        flight.setDestination("Delhi");
        flight.setDepartureDate(LocalDate.of(2024, 12, 25));
        flight.setDepartureTime("10:00 AM");
        flight.setArrivalTime("12:00 PM");
        flight.setAvailableSeats(100);
        flight.setFare(5000.0);
        flight.setId(flightId);

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        // Act
        Flight result = flightService.getById(flightId);

        // Assert
        assertNotNull(result);
        assertEquals(flightId, result.getId());
        assertEquals("AI123", result.getFlightNumber());
        verify(flightRepository, times(1)).findById(flightId);
    }

    @Test
    void updateFlight() {
        Long flightId=1L;
        Flight existingFlight = new Flight();
        existingFlight.setFlightNumber("AI123");
        existingFlight.setAirline("Air India");
        existingFlight.setSource("Mumbai");
        existingFlight.setDestination("Delhi");
        existingFlight.setDepartureDate(LocalDate.of(2024, 12, 25));
        existingFlight.setDepartureTime("10:00 AM");
        existingFlight.setArrivalTime("12:00 PM");
        existingFlight.setAvailableSeats(100);
        existingFlight.setFare(5000.0);
        existingFlight.setId(flightId);

        Flight updatedFlight = new Flight();
        updatedFlight.setFlightNumber("AI456");
        updatedFlight.setAirline("Air India");
        updatedFlight.setSource("Mumbai");
        updatedFlight.setDestination("Delhi");
        updatedFlight.setDepartureDate(LocalDate.of(2024, 12, 25));
        updatedFlight.setDepartureTime("10:00 AM");
        updatedFlight.setArrivalTime("12:00 PM");
        updatedFlight.setAvailableSeats(120);
        updatedFlight.setFare(5500.0);
        updatedFlight.setId(flightId);

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(existingFlight));
        when(flightRepository.save(any(Flight.class))).thenReturn(updatedFlight);

        // Act
        Flight result = flightService.updateFlight(flightId, flightDTO);

        // Assert
        assertNotNull(result);
        assertEquals(flightId, result.getId());
        assertEquals("AI456", result.getFlightNumber()); // Updated value
        assertEquals(120, result.getAvailableSeats());    // Updated value
        assertEquals(5500.0, result.getFare());        // Updated Value
        verify(flightRepository, times(1)).findById(flightId);
        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    @Test
    void deleteFlight() {
        Long flightId = 1L;
        when(flightRepository.existsById(flightId)).thenReturn(true);

        boolean result = flightService.deleteFlight(flightId);

        assertTrue(result);
        verify(flightRepository, times(1)).existsById(flightId);
        verify(flightRepository, times(1)).deleteById(flightId);
    }

    @Test
    void searchFlight() {
        // Arrange
        String source = "Mumbai";
        String destination = "Delhi";
        LocalDate departureDate = LocalDate.of(2024, 12, 25);

        List<Flight> flights = new ArrayList<>();
        Flight flight1 = new Flight();
        flight1.setFlightNumber("AI123");
        flight1.setAirline("Air India");
        flight1.setSource(source);
        flight1.setDestination(destination);
        flight1.setDepartureDate(departureDate);
        flight1.setDepartureTime("10:00 AM");
        flight1.setArrivalTime("12:00 PM");
        flight1.setAvailableSeats(100);
        flight1.setFare(5000.0);
        flight1.setId(1L);
        flights.add(flight1);

        when(flightRepository.findBySourceAndDestinationAndDepartureDate(source, destination, departureDate)).thenReturn(flights);

        List<Flight> result = flightService.searchFlight(source, destination, departureDate.toString());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("AI123", result.get(0).getFlightNumber());
        assertEquals(source, result.get(0).getSource());
        assertEquals(destination, result.get(0).getDestination());
        assertEquals(departureDate, result.get(0).getDepartureDate());
        verify(flightRepository, times(1)).findBySourceAndDestinationAndDepartureDate(source, destination, departureDate);
    }


}

