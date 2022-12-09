package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class User {
    private Long id;
    private String name;
    @Email(message = "Please enter correct E-mail")
    private String email;
}