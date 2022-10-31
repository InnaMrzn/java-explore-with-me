package ru.practicum.service;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto compilationDto);

    void delete(Long compId);

    void addEventToCompilation(Long compId, Long eventId);

    void removeEventFromCompilation(Long compId, Long eventId);

    CompilationDto findCompilation(Long compId);

    void pin(Long compId);

    void unpin(Long compId);

    List<CompilationDto> findCompilations(String pinned, int from, int size);

}
