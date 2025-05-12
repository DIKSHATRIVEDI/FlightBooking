package com.example.profilemanagement_service.dto;

import com.example.profilemanagement_service.model.Role;
import lombok.Data;

@Data
public class RegisterDTO {
    public String username;
    public String name;
    public String email;
    public String password;
    public Role role;
}
