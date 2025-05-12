package com.example.booking_service.controller;

import com.example.booking_service.dto.BookingDTO;
import com.example.booking_service.dto.FlightBookingDetailsDTO;
import com.example.booking_service.dto.PaymentResponseDTO;
import com.example.booking_service.dto.UserDTO;
import com.example.booking_service.exception.BookingException;
import com.example.booking_service.model.Booking;
import com.example.booking_service.service.BookingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid BookingDTO dto) {
        Optional<Booking> booking = bookingService.createBooking(dto);
        if (booking.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid Flight ID or No Seats Available");
        }
        return ResponseEntity.ok(booking.get());
    }
//@PostMapping("/create")
//public ResponseEntity<?> create(@RequestBody @Valid BookingDTO dto) {
//    Optional<PaymentResponseDTO> paymentResponse = bookingService.createBookingWithPayment(dto);
//
//    if (paymentResponse.isEmpty()) {
//        return ResponseEntity.badRequest().body("Invalid Flight ID or No Seats Available");
//    }
//
//    return ResponseEntity.ok(paymentResponse.get());
//}

    @GetMapping("/get/{id}")
    public ResponseEntity<Booking> getById(@PathVariable Long id) {//gets its personal booking
        Optional<Booking> booking=bookingService.getBookingById(id);
        return (booking.isPresent())?ResponseEntity.ok(booking.get()):ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> cancel(@PathVariable Long id) {
        boolean isCancelled = bookingService.cancelBooking(id);
        return isCancelled
                ? ResponseEntity.ok("Your booking with ID " + id + " has been cancelled.")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking with ID " + id + " not found.");
    }

    @GetMapping("/viewPassengers/{flightId}")
    @CircuitBreaker(name = "FLIGHT-AND-SEARCH-SERVICE", fallbackMethod = "flightFallback")
    public ResponseEntity<FlightBookingDetailsDTO> viewPassengersByFlight(@PathVariable Long flightId) throws Exception {
        FlightBookingDetailsDTO response = bookingService.getPassengersByFlightId(flightId);
        return (response == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(response);
    }

    @PutMapping("/confirm/{bookingId}")
    public ResponseEntity<String> confirmBooking(@PathVariable Long bookingId) {
        Optional<Booking> bookingOpt = bookingService.confirmBooking(bookingId);

        if (bookingOpt.isPresent()) {
            return ResponseEntity.ok("Booking confirmed with ID: " + bookingOpt.get().getId());
        } else {
            return ResponseEntity.badRequest().body("Booking confirmation failed. Please check the booking ID.");
        }
    }


    public ResponseEntity<String> flightFallback(Exception e) {
        System.out.println("Flight service is down, fallback triggered " + e.getMessage());
        return ResponseEntity.status(503).body("Flight service is down");
    }

    @GetMapping("/get/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingByUserId(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

}


