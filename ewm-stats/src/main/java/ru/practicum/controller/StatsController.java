package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.StatsService;

import java.util.List;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<List<ViewStatsDto>> getStats(
            @RequestParam(value = "start") String start,
            @RequestParam(value = "end") String end,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", required = false, defaultValue = "false") String unique) {

        return ResponseEntity.status(HttpStatus.OK).body(statsService.getStats(start, end, uris, unique));

    }

}
