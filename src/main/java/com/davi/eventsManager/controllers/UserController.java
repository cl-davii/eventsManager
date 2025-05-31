package com.davi.eventsManager.controllers;

import com.davi.eventsManager.models.entities.User;
import com.davi.eventsManager.models.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/events/users")
    private ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userRepository.save(user);
        return ResponseEntity.ok().body(newUser);
    };

    @GetMapping("/events/users")
    private List<User> listAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/events/users/{email}")
    private ResponseEntity<User> findByEmail(@RequestBody @PathVariable String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/events/users/{email}")
    private ResponseEntity<User> deleteByEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
