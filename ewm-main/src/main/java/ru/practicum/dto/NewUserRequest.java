package ru.practicum.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Value
public class NewUserRequest {

    @NotEmpty(message = "имя не может быть пустым")
    String name;
    @NotEmpty(message = "email не может быть пустым")
    @Email(message = "неправильный формат email")
    String email;

}
