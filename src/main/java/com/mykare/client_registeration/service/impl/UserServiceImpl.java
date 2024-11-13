package com.mykare.client_registeration.service.impl;

import com.mykare.client_registeration.entity.User;
import com.mykare.client_registeration.model.UserValidate;
import com.mykare.client_registeration.repository.UserRepository;
import com.mykare.client_registeration.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public ResponseEntity<String> saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            log.error("Data exception: " + ex.getMessage());
            throw new IllegalArgumentException("Email already exists. Please use a different email.");
        } catch (Exception ex) {
            log.error("Exception caught: " + ex.getMessage());
//            return ResponseEntity.ok(ex.getMessage());
        }
        return ResponseEntity.ok("Saved");
    }

    public void deleteUser(String email) {
//        Optional<User> userOptional = userRepository.findByEmail(email);
        User userdet=userRepository.findByEmail(email);
//        if (!userdet.isPresent()) {
        if (userdet==null) {
            throw new IllegalArgumentException("No user found with this email :: " + email );
        }
//        User user = new User();
//        user = userdet.get();
        log.info("user id : " + userdet.getId());
        userRepository.deleteById(userdet.getId());
    }

    public boolean validate(UserValidate user) {

        User userdet = userRepository.findByEmail(user.getEmail());
        if (userdet != null) {
            return userdet.getPassword().equals(user.getPassword());
        }
        return false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
