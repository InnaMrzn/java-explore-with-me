package ru.practicum.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class NewCompilationDto {
    List<Long> events;
    private final boolean pinned;
    @NotEmpty
    private final String title;

}
