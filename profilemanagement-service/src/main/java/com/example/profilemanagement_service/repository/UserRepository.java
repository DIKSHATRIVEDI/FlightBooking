package com.example.profilemanagement_service.repository;

import com.example.profilemanagement_service.model.Role;
import com.example.profilemanagement_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(Role role);
    Optional<User> findById(Long id);

}
