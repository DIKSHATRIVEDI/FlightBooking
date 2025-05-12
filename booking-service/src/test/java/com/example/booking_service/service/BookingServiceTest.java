package com.example.booking_service.service;

import com.example.booking_service.dto.BookingDTO;
import com.example.booking_service.dto.FlightResponseDTO;
import com.example.booking_service.dto.PassengerDTO;
import com.example.booking_service.feign.FareInterface;
import com.example.booking_service.feign.FlightInterface;
import com.example.booking_service.model.Booking;
import com.example.booking_service.model.Passenger;
import com.example.booking_service.repository.BookingRepository;
import com.example.booking_service.repository.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @Mock
    BookingRepository bookingRepository;
    @Mock
    FlightInterface flightInterface;
    @Mock
    private FareInterface fareInterface;
    @Mock
    PassengerRepository passengerRepository;
    @InjectMocks
    BookingService bookingService;

    private BookingDTO bookingDTO;
    private FlightResponseDTO flightResponseDTO;
    private Booking savedBooking;

    @BeforeEach
    void setUp(){
        bookingDTO = new BookingDTO();
        bookingDTO.flightId = 1L;
        bookingDTO.passengers = List.of(
                new PassengerDTO("Riya", "Janna", "riya@gmail.com", 12345),
                new PassengerDTO("Siya", "Daak", "siya@gmail.com", 67890)
        );

        flightResponseDTO = new FlightResponseDTO();
        flightResponseDTO.id = 1L;
        flightResponseDTO.availableSeats = 10;
        flightResponseDTO.fare = 200.0;

        savedBooking = new Booking();
        savedBooking.setId(101L);
        savedBooking.setFlightId(1L);
        savedBooking.setNumberOfPassenegers(2);
        savedBooking.setBookingDate(LocalDateTime.now().toString());
        savedBooking.setStatus("PENDING"); // Changed to PENDING to match the method's logic
        savedBooking.setTotalFare(flightResponseDTO.fare * bookingDTO.passengers.size());
        List<Passenger> passengerList = new ArrayList<>();
        Passenger passenger1 = new Passenger();
        passenger1.setFirstName("Riya");
        passenger1.setLastName("Janna");
        passenger1.setEmail("riya@gmail.com");
        passenger1.setPassportNumber(Integer.valueOf(String.valueOf(12345)));
        passenger1.setFlightId(1L);
        passengerList.add(passenger1);

        Passenger passenger2 = new Passenger();
        passenger2.setFirstName("Siya");
        passenger2.setLastName("Daak");
        passenger2.setEmail("siya@gmail.com");
        passenger2.setPassportNumber(Integer.valueOf(String.valueOf(67890)));
        passenger2.setFlightId(1L);
        passengerList.add(passenger2);
        savedBooking.setPassengers(passengerList);    }

    @Test
    void createBooking() {
        when(flightInterface.getFlightById(bookingDTO.flightId)).thenReturn(flightResponseDTO);
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);
        when(flightInterface.updateAvailableSeats(bookingDTO.flightId, bookingDTO.passengers.size())).thenReturn(true);
        //when(fareInterface.createOrder(savedBooking.getId(), savedBooking.getTotalFare())).thenReturn(true); //Removed this line

        // Act
        Optional<Booking> result = bookingService.createBooking(bookingDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(savedBooking.getId(), result.get().getId());
        assertEquals(savedBooking.getFlightId(), result.get().getFlightId());
        assertEquals(savedBooking.getNumberOfPassenegers(), result.get().getNumberOfPassenegers());
        assertEquals(savedBooking.getTotalFare(), result.get().getTotalFare());
        assertEquals("PENDING", result.get().getStatus()); //expecting pending here
        // assertEquals(savedBooking.getStatus(), result.get().getStatus()); // Removed : Now expects "PENDING"
        verify(flightInterface, times(1)).getFlightById(bookingDTO.flightId);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(flightInterface, times(1)).updateAvailableSeats(bookingDTO.flightId, bookingDTO.passengers.size());
        verify(fareInterface, times(1)).createOrder(savedBooking.getId(), savedBooking.getTotalFare()); //Added this line
        verify(passengerRepository, times(1)).saveAll(any());
    }

    @Test
    void getBookingById() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(savedBooking));
        Optional<Booking> result = bookingService.getBookingById(1L);
        assertTrue(result.isPresent());
        assertEquals("CONFIRMED", result.get().getStatus());
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void cancelBooking() {
        when(bookingRepository.existsById(1L)).thenReturn(true);
        boolean result = bookingService.cancelBooking(1L);
        assertTrue(result);
        verify(bookingRepository, times(1)).deleteById(1L);
    }

    @Test
    void getPassengersByFlightId() {
    }
}