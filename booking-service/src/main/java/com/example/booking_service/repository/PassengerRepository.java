package com.example.booking_service.repository;

import com.example.booking_service.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    List<Passenger> findByFlightId(Long flightId);
    List<Passenger> findByBookingId(Long bookingId);
}
