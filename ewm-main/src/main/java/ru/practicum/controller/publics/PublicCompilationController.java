package ru.practicum.controller.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.service.CompilationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    ResponseEntity<CompilationDto> getCompilation(@PathVariable("compId") Long compId) {
        log.info("GET /compilations/" + compId);
        return ResponseEntity.status(HttpStatus.OK).body(compilationService.findCompilation(compId));
    }

    @GetMapping
    ResponseEntity<List<CompilationDto>> getCompilations(@Valid @RequestParam(value = "pinned", required = false) String pinned,
                                                         @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                         @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        log.info("GET /compilations");

        return ResponseEntity.status(HttpStatus.OK).body(compilationService.findCompilations(pinned, from, size));
    }

}
