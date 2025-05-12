package com.example.booking_service.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    public String orderId;
    public String key;
    public double amount;
    public Long bookingId;
}
