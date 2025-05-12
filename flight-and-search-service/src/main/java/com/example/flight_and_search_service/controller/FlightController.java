package com.example.flight_and_search_service.controller;

import com.example.flight_and_search_service.dto.FlightDTO;
import com.example.flight_and_search_service.dto.FlightResponseDTO;
import com.example.flight_and_search_service.model.Flight;
import com.example.flight_and_search_service.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flight")
public class FlightController {
    @Autowired
    private FlightService service;

    @GetMapping("/getAll")
    public List<Flight> getAll() {
        return service.getAllFlight();
    }

    @PostMapping("/create")
    public Flight create(@RequestBody @Valid FlightDTO dto) {
        System.out.println("Object is"+dto);
        return service.createFlight(dto);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Flight> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Flight> update(@PathVariable Long id, @RequestBody FlightDTO flightDTO) {
        Flight flight=service.updateFlight(id, flightDTO);
        return (flight!=null)?ResponseEntity.ok(flight)
                :ResponseEntity.notFound().build();
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.deleteFlight(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
    @GetMapping("/search")
    public List<FlightResponseDTO> search(@RequestParam String source, @RequestParam String destination,
                                          @RequestParam String departureDate) {
        List<Flight> flights=service.searchFlight(source, destination, departureDate);
        return flights.stream().map(FlightResponseDTO::new).toList();
    }

    @PutMapping("/updateSeats/{id}")
    public ResponseEntity<Void> updateSeats(@PathVariable Long id, @RequestParam int seatsToReduce) {
        boolean updated = service.updateSeats(id, seatsToReduce);
        if(!updated) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().build();
    }

}