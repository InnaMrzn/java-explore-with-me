package ru.practicum.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class NewCompilationDto {
    List<Long> events;
    private final boolean pinned;
    @NotEmpty
    @Size(min=1, max=50)
    private final String title;

}
