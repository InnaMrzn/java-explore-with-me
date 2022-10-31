package ru.practicum.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompilationDto {

    private List<EventShortDto> events;
    private final Long id;
    private final Boolean pinned;
    private final String title;

}
