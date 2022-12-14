package ru.practicum.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class NewCategoryDto {
    @Size(min = 1, max = 200)
    @NotBlank
    private String name;
}
