package com.mgmt.userprofile.controller;

import com.mgmt.userprofile.models.User;
import com.mgmt.userprofile.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserControllerTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @Test
    void createUser() {
        User user = new User();
        when(userService.create(any(User.class))).thenReturn(user);
        assertDoesNotThrow(() -> userController.createUser(user));
    }
}