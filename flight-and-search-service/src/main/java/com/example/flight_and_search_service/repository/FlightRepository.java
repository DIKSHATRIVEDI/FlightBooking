package com.example.flight_and_search_service.repository;

import com.example.flight_and_search_service.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE f.source = :source AND f.destination = :destination AND DATE(f.departureDate) = :departureDate")
    List<Flight> findBySourceAndDestinationAndDepartureDate(
        @Param("source") String source,
        @Param("destination") String destination,
        @Param("departureDate") LocalDate departureDate);

    Optional<Flight> findByFlightNumber(String flightNumber);

}
