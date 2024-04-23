package com.mgmt.userprofile.service;

import com.mgmt.userprofile.Repository.UserRepository;
import com.mgmt.userprofile.models.Role;
import com.mgmt.userprofile.models.User;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public User create(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Role role = new Role();
        role.setRoleName("ROLE_USER");
        user.setRoles(Set.of(role));
        return userRepository.save(user);
    }

    public User getUserDetails(User request) {
        Optional<User> userOptional = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
        return userOptional.orElse(null);
    }
    public Optional<User> getUserDetailsByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
