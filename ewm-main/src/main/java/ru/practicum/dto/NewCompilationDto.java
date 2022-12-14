package ru.practicum.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class NewCompilationDto {
    List<Long> events;
    private final boolean pinned;
    @NotBlank
    @Size(max = 200)
    private final String title;

}
