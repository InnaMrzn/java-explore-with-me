package ru.practicum.dto;

import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
public class CategoryDto {
    Long id;
    @NotEmpty
    String name;
}
