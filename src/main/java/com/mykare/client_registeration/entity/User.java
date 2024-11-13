package com.mykare.client_registeration.entity;

import jakarta.persistence.*;
//import jakarta.
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;



@Entity
@Table(name = "users")
@Data
@Valid
public class User {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Username cannot be empty")
    @Column(nullable = false)

    private String username;

    @NotBlank(message = "password cannot be empty")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$",
            message = "Password must be at least 8 characters long, include a number, an uppercase letter, a lowercase letter, and a special character.")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "gender cannot be empty")
    @Column(nullable = false)
    private String gender;

    @NotBlank(message = "email cannot be empty")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

}
