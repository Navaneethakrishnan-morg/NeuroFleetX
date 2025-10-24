package com.neurofleetx.controller;

import com.neurofleetx.model.User;
import com.neurofleetx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        // Set password to original for admin to see credentials
        return ResponseEntity.ok(users);
    }

    @GetMapping("/admin/users/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable User.UserRole role) {
        return ResponseEntity.ok(userRepository.findByRole(role));
    }

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/admin/users/{id}/toggle-active")
    public ResponseEntity<User> toggleUserActive(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setActive(!user.getActive());
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
