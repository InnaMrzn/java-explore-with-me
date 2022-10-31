package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCompilationController {

    private final CompilationService compilationService;

    @DeleteMapping("/{compId}/events/{eventId}")
    public void removeEventFromCompilation(@PathVariable("compId") Long compId,
                                           @PathVariable("eventId") Long eventId) {
        log.info("DELETE/" + compId + "compilations/" + eventId);
        compilationService.removeEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void addEventToCompilation(@PathVariable("compId") Long compId,
                                      @PathVariable("eventId") Long eventId) {
        log.info("PATCH/" + compId + "compilations/" + eventId);
        compilationService.addEventToCompilation(compId, eventId);
    }


    @DeleteMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteCompilation(@PathVariable("compId") Long compId) {
        log.info("DELETE /compilations/" + compId);
        compilationService.delete(compId);

    }

    @PatchMapping("/{compId}/pin")
    @ResponseStatus(value = HttpStatus.OK)
    public void pin(@PathVariable("compId") Long compId) {
        log.info("PATCH /compilations/" + compId + "pin");
        compilationService.pin(compId);
    }

    @DeleteMapping("/{compId}/pin")
    @ResponseStatus(value = HttpStatus.OK)
    public void unpin(@PathVariable("compId") Long compId) {
        log.info("DELETE /compilations/" + compId + "pin");
        compilationService.unpin(compId);
    }

    @PostMapping
    public ResponseEntity<CompilationDto> saveCompilation(@Valid @RequestBody NewCompilationDto body) {
        log.info("POST /compilations");
        CompilationDto dto = compilationService.create(body);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }


}
