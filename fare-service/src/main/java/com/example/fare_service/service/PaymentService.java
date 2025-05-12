package com.example.fare_service.service;

import com.example.fare_service.dto.RequestDTO;
import com.example.fare_service.dto.ResponseDTO;
import com.example.fare_service.feign.BookingInterface;
import com.example.fare_service.model.Payment;
import com.example.fare_service.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
public class PaymentService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    BookingInterface bookingInterface;

    public ResponseDTO createOrder(Long bookingId, Double amount) throws Exception {
        if (bookingId == null || amount == null) {
            System.out.println("Booking id is null");
        }

        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_"+bookingId);

        Order order = client.orders.create(orderRequest);
        savePayment(order.get("id"), amount, "INR",order.get("status"));

        System.out.println("Creating payment order for bookingId = " + bookingId + ", amount = " + amount);
        System.out.println("Order ID: " + order.get("id"));
        System.out.println("Order status: " + order.get("status"));

        return new ResponseDTO(
                order.get("id"),
                order.get("status"),
                keyId,
                amount.intValue() * 100
                );
    }

    public boolean verifySignature(String orderId, String paymentId, String signatureFromRazorpay) {
        try {
            String data = orderId + "|" + paymentId;
            SecretKeySpec keySpec = new SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            byte[] hash = mac.doFinal(data.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString().equals(signatureFromRazorpay);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void savePayment(String orderId, double amount, String currency, String status) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setStatus(status);
        paymentRepository.save(payment);
    }

    public String handlePaymentSuccess(String bookingId) {
        try {
            String response = bookingInterface.confirmBooking(Long.parseLong(bookingId));
            return response;
        } catch (Exception e) {
            return "Payment Done, but Booking Confirmation Failed: " + e.getMessage();
        }
    }

}

