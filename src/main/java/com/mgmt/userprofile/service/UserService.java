package com.mgmt.userprofile.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgmt.userprofile.Repository.UserRepository;
import com.mgmt.userprofile.exception.UserAlreadyExistsException;
import com.mgmt.userprofile.models.Role;
import com.mgmt.userprofile.models.User;
import com.mgmt.userprofile.models.UserCred;
import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Transient
    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: "+user.getEmail());
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Role role = new Role();
        role.setRoleName("ROLE_USER");
        user.setRoles(Set.of(role));
        User savedUser = userRepository.save(user);
        String userCredentialsJson;
        try {
            userCredentialsJson = objectMapper.writeValueAsString(new UserCred(user.getEmail(), user.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException(" failed while parsing");
        }
        kafkaTemplate.send("AUTH_TOPIC", userCredentialsJson);
        return savedUser;
    }

    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }
}
