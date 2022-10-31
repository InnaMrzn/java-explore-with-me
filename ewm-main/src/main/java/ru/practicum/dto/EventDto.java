package ru.practicum.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder

public abstract class EventDto {
    private final String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private final String eventDate;
    private final Long id;
    private UserShortDto initiator;
    private final String paid;
    private final String title;
    private long views;
}
