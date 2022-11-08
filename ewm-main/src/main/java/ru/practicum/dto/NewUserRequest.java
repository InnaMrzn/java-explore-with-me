package ru.practicum.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class NewUserRequest {

    @NotBlank(message = "имя не может быть пустым")
    String name;
    @NotBlank(message = "email не может быть пустым")
    @Email(message = "неправильный формат email")
    @Size(max = 320)
    String email;

}
