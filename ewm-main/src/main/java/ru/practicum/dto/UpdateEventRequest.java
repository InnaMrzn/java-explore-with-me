package ru.practicum.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
public class UpdateEventRequest {
    Long eventId;
    @NotEmpty(message = "аннотация не может быть пустой")
    @Size(min = 20, max = 2000, message="размер аннотации должен быть от 20 до 2000 знаков")
    String annotation;
    @NotNull(message = "категория не может быть пустой")
    Long category;
    @NotEmpty(message = "описание не может быть пустым")
    @Size(min = 20, max = 7000, message="размер описания должен быть от 20 до 7000 знаков")
    String description;
    @NotEmpty(message = "дата не может быть пустой")
    String eventDate;
    Boolean paid;
    @PositiveOrZero
    Integer participantLimit;
    @NotEmpty(message = "заголовок не может быть пустым")
    @Size(min = 3, max = 120, message="размер заголовка должен быть от 3 до 120 знаков")
    String title;
}