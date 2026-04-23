package com.sahil.assignment.controller;

import com.sahil.assignment.dto.ApiResponse;
import com.sahil.assignment.model.User;
import com.sahil.assignment.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody User user) {
        User saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("User is created successfully", saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(ApiResponse.ok("User fetched", user));
    }
}