package ru.practicum.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.*;
import ru.practicum.dto.mappers.CompilationMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repo.CompilationRepository;
import ru.practicum.repo.EventRepository;
import ru.practicum.service.CompilationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationDto);

        List<Event> events = eventRepository.findAllById(compilationDto.getEvents());
        compilation.setEvents(events);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Компиляция с id " + compId + "не найдена."));

        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public void pin(Long compId) {
        Compilation comp = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Компиляция с id " + compId + "не найдена."));

        if (!comp.getPinned()) {
            comp.setPinned(true);
            compilationRepository.save(comp);
        }
    }

    @Override
    @Transactional
    public void unpin(Long compId) {
        Compilation comp = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Компиляция с id " + compId + "не найдена."));

        if (comp.getPinned()) {
            comp.setPinned(false);
            compilationRepository.save(comp);
        }
    }

    @Override
    @Transactional
    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation comp = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Компиляция с id=" + compId + " не найдена."));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id=" + eventId + " не найдено."));

        comp.addEvent(event);
        compilationRepository.save(comp);
    }

    @Override
    @Transactional
    public void removeEventFromCompilation(Long compId, Long eventId) {
        Compilation comp = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Компиляция с id=" + compId + " не найдена."));

        comp.removeEvent(eventId);
        compilationRepository.save(comp);
    }

    @Override
    public CompilationDto findCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Компиляция с Id=" + compId + " не найдена."));
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> findCompilations(String pinned, int from, int size) {
        int page = from / size;
        Pageable paging = PageRequest.of(page, size);
        List<Compilation> compilations;
        if (pinned != null && !pinned.equalsIgnoreCase("null")) {

            compilations = compilationRepository.findAllByPinned(Boolean.parseBoolean(pinned), paging).getContent();
        } else {
            compilations = compilationRepository.findAll(paging).getContent();
        }
        return compilations.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }
}
