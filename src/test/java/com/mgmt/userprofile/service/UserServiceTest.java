package com.mgmt.userprofile.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgmt.userprofile.Repository.UserRepository;
import com.mgmt.userprofile.models.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private KafkaTemplate<String,String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void testCreate() {
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encode password");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(kafkaTemplate.send(anyString(),anyString())).thenReturn(new CompletableFuture<>());
        assertDoesNotThrow(() -> userService.create(new User()));
    }

    @Test
    void testCreateFailedWhileParsing() throws JsonProcessingException {
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encode password");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
        when(kafkaTemplate.send(anyString(),anyString())).thenReturn(new CompletableFuture<>());
        assertThrows(RuntimeException.class, () -> userService.create(new User()));
    }
}