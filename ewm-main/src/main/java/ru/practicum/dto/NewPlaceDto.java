package ru.practicum.dto;

import lombok.Data;
import ru.practicum.model.Location;

import javax.validation.constraints.*;

@Data
public class NewPlaceDto {
    @NotBlank(message = "имя не может быть пустым")
    String name;
    @NotNull(message = "локация должна быть указана")
    Location location;

}
