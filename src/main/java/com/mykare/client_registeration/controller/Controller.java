package com.mykare.client_registeration.controller;

import com.mykare.client_registeration.entity.User;
import com.mykare.client_registeration.model.CommonResponse;
import com.mykare.client_registeration.model.UserDelete;
import com.mykare.client_registeration.model.UserValidate;
import com.mykare.client_registeration.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/rest")
public class Controller {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public CommonResponse saveUser(@RequestBody User user) {
        CommonResponse validationResponse = validateUserDetails(user);
        if (validationResponse.getStatus() != 200) {
            return validationResponse;
        }

        return userService.saveUser(user);
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>>deleteUser(@RequestBody UserDelete userDelete) throws Exception {
        try{
         userService.deleteUser(userDelete.getEmail());

    } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No user found with this email :: " + userDelete.getEmail());
    }catch (Exception e){
            throw new Exception("Server encountered an error" + e);
        }
        return createResponse(200,"User deleted");
    }

    @PostMapping("/validate")
    public CommonResponse validateUser(@Validated @RequestBody  UserValidate userValidate,BindingResult bindingResult) throws Exception {
        try {
//
//            if (bindingResult.hasErrors()) {
//                StringBuilder errorMessages = new StringBuilder();
//                bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append(" "));
//                return createResponse(001, errorMessages.toString());
//            }
            return userService.validate(userValidate);
//            if (isValid) {
//                return createResponse(200, "Success");
//            } else {
//                return createResponse(1001, "invalid credentials");
//            }
        }catch(Exception ex){
            throw new Exception("Server encountered an error" + ex);
        }
    }

    @GetMapping("/getClients")
    public ResponseEntity<List<User>> getAllClients() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    private ResponseEntity<Map<String, Object>> createResponse(int status,String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
    private CommonResponse validateUserDetails(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return new CommonResponse(400, "Username cannot be null or empty.");
        }
        if (!isValidPassword(user.getPassword())) {
            return new CommonResponse(400, "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }
        if (user.getGender() == null || user.getGender().isEmpty()) {
            return new CommonResponse(400, "Gender cannot be null or empty.");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()||!isValidEmail(user.getEmail())) {
            return new CommonResponse(400, "Email cannot be null,empty or invalid.");
        }
        return new CommonResponse(200, "Validation successful.");
    }



    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        return password != null && password.matches(passwordPattern);
    }
}


