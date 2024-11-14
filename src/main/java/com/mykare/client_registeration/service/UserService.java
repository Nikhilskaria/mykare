package com.mykare.client_registeration.service;

import com.mykare.client_registeration.entity.User;
import com.mykare.client_registeration.model.CommonResponse;
import com.mykare.client_registeration.model.UserValidate;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService{
    CommonResponse saveUser(User user);
     void deleteUser(String email);
    CommonResponse validate(UserValidate user);

    List<User> getAllUsers();
}
