package com.mykare.client_registeration.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Valid
public class UserValidate {

    private String email;

//    @NotBlank(message = "Password cannot be empty")
    private String password;
}
