package ru.practicum.dto;

import lombok.Data;

@Data
public class UpdateEventRequest {
    Long eventId;
    String annotation;
    Long category;
    String description;
    String eventDate;
    Boolean paid;
    Integer participantLimit;
    String title;
}