package com.mykare.client_registeration.service.impl;

import com.mykare.client_registeration.entity.User;
import com.mykare.client_registeration.model.CommonResponse;
import com.mykare.client_registeration.model.UserValidate;
import com.mykare.client_registeration.repository.UserRepository;
import com.mykare.client_registeration.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public CommonResponse saveUser(User user) {
        try {
            userRepository.save(user);
            return new CommonResponse(200, "User registered successfully.");
        } catch (DataIntegrityViolationException ex) {
            log.error("Data exception: " + ex.getMessage());
            return new CommonResponse(400, "Email already exists. Please use a different email.");
        } catch (Exception ex) {
            log.error("Exception caught: " + ex.getMessage());
            return new CommonResponse(500, "Server encountered an error: " + ex.getMessage());
        }
    }
    public void deleteUser(String email) {
//        Optional<User> userOptional = userRepository.findByEmail(email);
        User userDetails = userRepository.findByEmail(email);
//        if (!userDetails.isPresent()) {
        if (userDetails == null) {
            throw new IllegalArgumentException("No user found with this email :: " + email );
        }
//        User user = new User();
//        user = userDetails.get();
        log.info("user id : " + userDetails.getId());
        userRepository.deleteById(userDetails.getId());
    }

    public CommonResponse validate(UserValidate user) {
        User userDetails = userRepository.findByEmail(user.getEmail());

        if (userDetails == null) {
            return new CommonResponse(404, "User not found.");
        }

        if (userDetails.getPassword().equals(user.getPassword())) {
            return new CommonResponse(200, "Validation successful.");
        } else {
            return new CommonResponse(401, "Invalid credentials.");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
