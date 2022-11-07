package ru.practicum.dto;

import lombok.Value;
import ru.practicum.model.Location;

@Value
public class PlaceDto {
    Long id;
    String name;
    Location location;
}
