package ru.practicum.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class NewUserRequest {

    @NotEmpty(message = "имя не может быть пустым")
    String name;
    @NotEmpty(message = "email не может быть пустым")
    @Email(message = "неправильный формат email")
    @Size(max = 320)
    String email;

}
