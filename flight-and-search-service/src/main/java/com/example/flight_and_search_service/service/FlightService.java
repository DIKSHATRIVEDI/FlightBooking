package com.example.flight_and_search_service.service;

import com.example.flight_and_search_service.dto.FlightDTO;
import com.example.flight_and_search_service.exception.*;
import com.example.flight_and_search_service.model.Flight;
import com.example.flight_and_search_service.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {
    @Autowired
    FlightRepository flightRepository;

    public Flight createFlight(FlightDTO flightDTO){
        try {
            Optional<Flight> existingFlight = flightRepository.findByFlightNumber(flightDTO.flightNumber);
            if (existingFlight.isPresent()) {
                throw new FlightCreationException("Flight with number " + flightDTO.flightNumber + " already exists");
            }

            Flight flight = new Flight();
            flight.setFlightNumber(flightDTO.flightNumber);
            flight.setAirline(flightDTO.airline);
            flight.setSource(flightDTO.source);
            flight.setDestination(flightDTO.destination);
            flight.setDepartureDate(flightDTO.departureDate);
            flight.setDepartureTime(flightDTO.departureTime);
            flight.setArrivalTime(flightDTO.arrivalTime);
            flight.setAvailableSeats(flightDTO.availableSeats);
            flight.setFare(flightDTO.fare);

            return flightRepository.save(flight);
        } catch (FlightCreationException e) {
            throw e;
        } catch (Exception e) {
            throw new FlightCreationException("Exception in createFlight: " + e.getMessage());
        }
    }

    public List<Flight> getAllFlight(){
        return flightRepository.findAll();
    }

    public Flight getById(Long id){
        try {
            Optional<Flight> flightOptional = flightRepository.findById(id);
            if (flightOptional.isPresent()) {
                return flightOptional.get();
            } else {
                throw new FlightNotFoundException("Flight with ID " + id + " not found");
            }
        } catch (Exception e) {
            throw new FlightNotFoundException("Exception in getById: " + e.getMessage());
        }
    }

    public Flight updateFlight(Long id, FlightDTO flightDTO){
        try {
            Optional<Flight> optionalFlight = flightRepository.findById(id);
            if (optionalFlight.isEmpty()) {
                throw new FlightNotFoundException("Flight with ID " + id + " not found");
            }

            Flight flight = optionalFlight.get();
            flight.setFlightNumber(flightDTO.flightNumber);
            flight.setAirline(flightDTO.airline);
            flight.setSource(flightDTO.source);
            flight.setDestination(flightDTO.destination);
            flight.setDepartureDate(flightDTO.departureDate);
            flight.setDepartureTime(flightDTO.departureTime);
            flight.setArrivalTime(flightDTO.arrivalTime);
            flight.setAvailableSeats(flightDTO.availableSeats);
            flight.setFare(flightDTO.fare);

            return flightRepository.save(flight);
        } catch (Exception e) {
            throw new FlightUpdateException("Exception in updateFlight: " + e.getMessage());
        }

    }

    public boolean deleteFlight(Long id){
        try {
            if (!flightRepository.existsById(id)) {
                throw new FlightNotFoundException("Flight with ID " + id + " not found");
            }
            flightRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new FlightDeletionException("Exception in deleteFlight: " + e.getMessage());
        }
    }
    public List<Flight> searchFlight(String source, String destination, String departureDate){
        LocalDate date=LocalDate.parse(departureDate);
        System.out.println("Searching flights from " + source + " to " + destination + " on " + date);
        return flightRepository.findBySourceAndDestinationAndDepartureDate(source,
                destination, date);
    }
    public boolean updateSeats(Long flightId, int seatsToReduce) {
        try {
            Optional<Flight> flightOptional = flightRepository.findById(flightId);
            if (flightOptional.isEmpty()) {
                throw new FlightNotFoundException("Flight with ID " + flightId + " not found");
            }
            Flight flight = flightOptional.get();
            int available = flight.getAvailableSeats();
            if (available < seatsToReduce) {
                throw new SeatUpdateException("Not enough seats. Requested: " + seatsToReduce + ", Available: " + available);
            }
            flight.setAvailableSeats(available - seatsToReduce);
            flightRepository.save(flight);
            return true;
        } catch (Exception e) {
            throw new SeatUpdateException("Exception in updateSeats: " + e.getMessage());
        }
    }
}

