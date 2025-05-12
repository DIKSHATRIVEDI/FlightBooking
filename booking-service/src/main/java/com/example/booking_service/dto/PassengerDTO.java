package com.example.booking_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PassengerDTO {
    @NotBlank(message = "First name is required")
    public String firstName;

    @NotBlank(message = "Last name is required")
    public String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    public String email;

    @NotBlank(message = "Gender must not be blank")
    public String gender;

    @NotNull(message = "Aadhar number must not be null")
    @Digits(integer = 12, fraction = 0, message = "Aadhar number must be numeric and 12 digits")
    public Long aadharNumber;
}
