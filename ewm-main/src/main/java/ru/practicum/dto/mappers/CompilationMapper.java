package ru.practicum.dto.mappers;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.model.Compilation;

import java.util.stream.Collectors;

public class CompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compDto = new CompilationDto(compilation.getId(), compilation.getPinned(),
                compilation.getTitle());
        compDto.setEvents(compilation.getEvents().stream().map(EventMapper::toEventShortDto)
                .collect(Collectors.toList()));
        return compDto;
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        Compilation comp = new Compilation();
        comp.setTitle(newCompilationDto.getTitle());
        comp.setPinned(newCompilationDto.isPinned());
        return comp;
    }
}
