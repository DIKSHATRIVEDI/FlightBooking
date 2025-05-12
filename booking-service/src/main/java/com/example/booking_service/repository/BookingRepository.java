package com.example.booking_service.repository;

import com.example.booking_service.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b JOIN b.passengers p WHERE b.flightId = :flightId AND p.aadharNumber = :aadharNumber")
    Optional<Booking> findByFlightIdAndAadharNumber(@Param("flightId") Long flightId, @Param("aadharNumber") Long aadharNumber);

    List<Booking> findByUserId(Long userId);

}
