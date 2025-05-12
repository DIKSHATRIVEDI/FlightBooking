package com.example.booking_service.feign;

import com.example.booking_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PROFILEMANAGEMENT-SERVICE")
public interface ProfileInterface {

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserDTO> getUserByUserId(@PathVariable Long id);
}
