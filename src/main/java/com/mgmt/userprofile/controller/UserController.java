package com.mgmt.userprofile.controller;

import com.mgmt.userprofile.models.User;
import com.mgmt.userprofile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User request) {
        User user = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/user-details")
    public ResponseEntity<User> getUserDetails(@RequestBody User request) {
        User user = userService.getUserDetails(request);
        return ResponseEntity.ok(user);
    }
    @GetMapping("/user-details/email")
    public ResponseEntity<Optional<User>> getUserDetailsByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserDetailsByEmail(email));
    }
}
