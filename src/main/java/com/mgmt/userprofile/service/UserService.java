package com.mgmt.userprofile.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgmt.userprofile.Repository.UserRepository;
import com.mgmt.userprofile.models.Role;
import com.mgmt.userprofile.models.User;
import com.mgmt.userprofile.models.UserCred;
import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Transient
    public User create(User user) {
        //String encodedPassword = passwordEncoder.encode(user.getPassword());
        //user.setPassword(encodedPassword);
        Role role = new Role();
        role.setRoleName("ROLE_USER");
        user.setRoles(Set.of(role));
        User savedUser = userRepository.save(user);
        ObjectMapper objectMapper = new ObjectMapper();
        String userCredentialsJson;
        try {
            userCredentialsJson = objectMapper.writeValueAsString(new UserCred(user.getEmail(), user.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException(" failed while parsing");
        }
        kafkaTemplate.send("AUTH_TOPIC", userCredentialsJson);
        return savedUser;
    }
}
