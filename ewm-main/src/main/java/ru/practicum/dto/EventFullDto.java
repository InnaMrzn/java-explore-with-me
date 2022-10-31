package ru.practicum.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.model.Location;


@Data
@SuperBuilder
public class EventFullDto extends EventDto {
    private String createdOn;
    private final String description;
    private Location location;
    private final Integer participantLimit;
    private String publishedOn;
    private final String requestModeration;
    private final String state;

}
