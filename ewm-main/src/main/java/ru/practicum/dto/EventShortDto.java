package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventShortDto {
    private final String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private final String eventDate;
    private final Long id;
    private UserShortDto initiator;
    private final String paid;
    private final String title;
    private Long views;

}
