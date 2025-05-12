package com.example.booking_service.feign;

import com.example.booking_service.dto.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("FARE-SERVICE")
public interface FareInterface {
//    @PostMapping("/api/payment/create-order")
//    public PaymentResponseDTO createOrder(@RequestParam Long bookingId, @RequestParam Double amount);

}
