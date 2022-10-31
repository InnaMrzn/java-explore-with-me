package ru.practicum.dto;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class CategoryDto {
    Long id;
    String name;
}
