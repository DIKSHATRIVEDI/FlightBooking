package com.example.fare_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("BOOKING-SERVICE")
public interface BookingInterface {
    @PutMapping("booking/confirm/{bookingId}")
    public String confirmBooking(@PathVariable("bookingId") Long bookingId);
}

