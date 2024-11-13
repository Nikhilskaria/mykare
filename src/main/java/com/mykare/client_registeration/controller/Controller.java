package com.mykare.client_registeration.controller;

import com.mykare.client_registeration.entity.User;
import com.mykare.client_registeration.model.UserDelete;
import com.mykare.client_registeration.model.UserValidate;
import com.mykare.client_registeration.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rest")
public class Controller {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> saveUser(@Validated @RequestBody User user, BindingResult bindingResult) throws Exception {
        try{

            if (bindingResult.hasErrors()) {
                StringBuilder errorMessages = new StringBuilder();
                bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append(" "));
                return createResponse(001, errorMessages.toString());
            }
        userService.saveUser(user);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Please ensure the data is correct");
        }catch (Exception e){
            throw new Exception("Server encountered an error" + e);
        }
        return createResponse(200,"client registered successfully");
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
    public ResponseEntity<Map<String, Object>>validateUser(@Validated @RequestBody  UserValidate userValidate,BindingResult bindingResult) throws Exception {
        try {

            if (bindingResult.hasErrors()) {
                StringBuilder errorMessages = new StringBuilder();
                bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append(" "));
                return createResponse(001, errorMessages.toString());
            }
            boolean isValid = userService.validate(userValidate);
            if (isValid) {
                return createResponse(200, "Success");
            } else {
                return createResponse(1001, "invalid credentials");
            }
        }catch(Exception ex){
            throw new Exception("Server encountered an error" + ex);
        }
    }

    @GetMapping("/getClients")
    public ResponseEntity<List<User>> getAllClients() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);  // Return a list of users as the response body
    }
    public ResponseEntity<Map<String, Object>> createResponse(int status,String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);
//        response.put("data", data);
        return ResponseEntity.ok(response);
    }
}


