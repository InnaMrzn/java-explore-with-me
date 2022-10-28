package ru.practicum.dto;

import lombok.Value;
import ru.practicum.model.Location;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class NewEventDto {
    @NotEmpty(message = "аннотация не может быть пустой")
    @Size(min = 20, max = 2000)
    String annotation;
    @NotNull(message = "категория не может быть пустой")
    Long category;
    @NotEmpty(message = "описание не может быть пустым")
    @Size(min = 20, max = 7000)
    String description;
    @NotEmpty(message = "дата не может быть пустой")
    String eventDate;
    @NotNull(message = "локация должна быть указана")
    Location location;
    boolean paid;
    int participantLimit;
    boolean requestModeration;
    @NotEmpty(message = "заголовок не может быть пустым")
    @Size(min = 3, max = 120)
    String title;

}