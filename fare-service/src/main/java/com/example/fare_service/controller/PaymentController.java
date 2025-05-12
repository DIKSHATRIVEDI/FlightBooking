package com.example.fare_service.controller;

import com.example.fare_service.dto.RequestDTO;
import com.example.fare_service.dto.ResponseDTO;
import com.example.fare_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseDTO createOrder(@RequestParam Long bookingId, @RequestParam Double amount) throws Exception {
        return paymentService.createOrder(bookingId, amount);
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, String> data) {
        String orderId = data.get("razorpay_order_id");
        String paymentId = data.get("razorpay_payment_id");
        String signature = data.get("razorpay_signature");
        double amount=Integer.parseInt(data.get("amount"));

        boolean isValid = paymentService.verifySignature(orderId, paymentId, signature);

        if (isValid) {
            return ResponseEntity.ok(" Payment Verified Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Verification Failed");
        }
    }

    @PostMapping("/payment-success")
    public ResponseEntity<String> handlePaymentSuccess(@RequestParam("bookingId") String bookingId) {
        String responseMessage = paymentService.handlePaymentSuccess(bookingId);
        System.out.println("message is "+responseMessage);
        if (responseMessage.contains("Failed")) {
            return ResponseEntity.status(500).body(responseMessage);
        } else {
            return ResponseEntity.ok(responseMessage);
        }
    }

}
