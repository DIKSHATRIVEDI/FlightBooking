package com.example.flight_and_search_service.controller;

import com.example.flight_and_search_service.dto.FlightDTO;
import com.example.flight_and_search_service.dto.FlightResponseDTO;
import com.example.flight_and_search_service.model.Flight;
import com.example.flight_and_search_service.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class FlightControllerTest {

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    private FlightDTO flightDTO;
    private Flight flight;
    private Long flightId = 1L;

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

        flight = new Flight();
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
    }

    @Test
    void getAllFlights() {
        // Arrange
        List<Flight> flights = new ArrayList<>();
        flights.add(flight);
        when(flightService.getAllFlight()).thenReturn(flights);

        // Act
        List<Flight> result = flightController.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(flight, result.get(0));
        verify(flightService, times(1)).getAllFlight();
    }

    @Test
    void createFlight() {
        // Arrange
        when(flightService.createFlight(flightDTO)).thenReturn(flight);

        // Act
        Flight result = flightController.create(flightDTO);

        // Assert
        assertNotNull(result);
        assertEquals(flight, result);
        verify(flightService, times(1)).createFlight(flightDTO);
    }

    @Test
    void getFlightById() {
        // Arrange
        when(flightService.getById(flightId)).thenReturn(flight);
        ResponseEntity<Flight> expectedResponse = ResponseEntity.ok(flight);

        // Act
        ResponseEntity<Flight> result = flightController.getById(flightId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result);
        verify(flightService, times(1)).getById(flightId);
    }

    @Test
    void updateFlight() {
        // Arrange
        when(flightService.updateFlight(flightId, flightDTO)).thenReturn(flight);
        ResponseEntity<Flight> expectedResponse = ResponseEntity.ok(flight);

        // Act
        ResponseEntity<Flight> result = flightController.update(flightId, flightDTO);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result);
        verify(flightService, times(1)).updateFlight(flightId, flightDTO);
    }

    @Test
    void deleteFlight() {
        // Arrange
        when(flightService.deleteFlight(flightId)).thenReturn(true);
        ResponseEntity<Void> expectedResponse = ResponseEntity.noContent().build();

        // Act
        ResponseEntity<Void> result = flightController.delete(flightId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertEquals(expectedResponse, result);
        verify(flightService, times(1)).deleteFlight(flightId);
    }

    @Test
    void searchFlight() {
        // Arrange
        String source = "Mumbai";
        String destination = "Delhi";
        String departureDate = "2024-12-25";
        List<Flight> flights = new ArrayList<>();
        flights.add(flight);
        List<FlightResponseDTO> flightResponseDTOs = new ArrayList<>();
        flightResponseDTOs.add(new FlightResponseDTO(flight));

        when(flightService.searchFlight(source, destination, departureDate)).thenReturn(flights);

        // Act
        List<FlightResponseDTO> result = flightController.search(source, destination, departureDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        FlightResponseDTO actual = result.get(0);
        FlightResponseDTO expected = flightResponseDTOs.get(0);
        verify(flightService, times(1)).searchFlight(source, destination, departureDate);
    }

    @Test
    void updateSeats() {
        // Arrange
        Long flightId = 1L;
        int seatsToReduce = 30;
        when(flightService.updateSeats(flightId, seatsToReduce)).thenReturn(true);
        ResponseEntity<Void> expectedResponse = ResponseEntity.ok().build();

        // Act
        ResponseEntity<Void> result = flightController.updateSeats(flightId, seatsToReduce);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result);
        verify(flightService, times(1)).updateSeats(flightId, seatsToReduce);
    }
}
