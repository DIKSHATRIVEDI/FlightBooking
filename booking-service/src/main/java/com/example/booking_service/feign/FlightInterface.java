package com.example.booking_service.feign;

import com.example.booking_service.dto.FlightResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="FLIGHT-AND-SEARCH-SERVICE")
public interface FlightInterface {
    @GetMapping("/flight/get/{id}")
    public FlightResponseDTO getFlightById(@PathVariable Long id);
    @PutMapping("/flight/updateSeats/{id}")
    public ResponseEntity<Void> updateSeats(@PathVariable Long id, @RequestParam int seatsToReduce);
}
