package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.Location;


@Data
@Builder
public class EventFullDto {
    private final String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String createdOn;
    private final String description;
    private String eventDate;
    private final Long id;
    private UserShortDto initiator;
    private Location location;
    private final String paid;
    private final Integer participantLimit;
    private String publishedOn;
    private final String requestModeration;
    private final String state;
    private final String title;
    private Long views;

}
