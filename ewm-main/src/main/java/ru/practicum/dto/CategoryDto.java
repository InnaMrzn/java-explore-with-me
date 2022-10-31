package ru.practicum.dto;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class CategoryDto {
    Long id;
    @NotEmpty
    @Size(min = 1, max = 50, message = "название категории может быть длиной от 1 до 50 символов")
    String name;
}
